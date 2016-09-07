package com.tomatrocho.generator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

public class WorldGeneratorUI extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private Display display;
	
	private final JTextField widthField, heightField, frequencyField, birthLimitField, deathLimitField, seedField;
	private final JButton addSandCells, swapDisconnectedCavernsState, doStepButton, generateCaveButton;
	
	
	public WorldGeneratorUI(WorldGenerator generator) {
		setSize(1000, 1000);
		setLayout(new MigLayout());
		
		JPanel mapGenerationPanel = new JPanel(new MigLayout());
		
		JLabel widthLabel = new JLabel("Width");
		widthField = new JTextField(3);
		widthField.setText(String.valueOf(generator.getW()));
		mapGenerationPanel.add(widthLabel);
		mapGenerationPanel.add(widthField);
		
		JLabel heightLabel = new JLabel("Height");
		heightField = new JTextField(3);
		heightField.setText(String.valueOf(generator.getH()));
		mapGenerationPanel.add(heightLabel);
		mapGenerationPanel.add(heightField);
		
		JLabel seedLabel = new JLabel("Seed");
		seedField = new JTextField(15);
		seedField.setText(String.valueOf(generator.getSeed()));
		mapGenerationPanel.add(seedLabel, "gap unrelated");
		mapGenerationPanel.add(seedField);
		
		JLabel frequencyLabel = new JLabel("Frequency");
		frequencyField = new JTextField(3);
		frequencyField.setText(String.valueOf(generator.chanceToStartAlive));
		mapGenerationPanel.add(frequencyLabel, "gap unrelated");
		mapGenerationPanel.add(frequencyField);
	    
		JLabel birtLimitLabel = new JLabel("Birth limit");
		birthLimitField = new JTextField(1);
		birthLimitField.setText(String.valueOf(generator.birthLimit));
		mapGenerationPanel.add(birtLimitLabel, "gap unrelated");
		mapGenerationPanel.add(birthLimitField);
		
		JLabel deathLimitLabel = new JLabel("Death limit");
		deathLimitField = new JTextField(1);
		deathLimitField.setText(String.valueOf(generator.deathLimit));
		mapGenerationPanel.add(deathLimitLabel, "gap unrelated");
		mapGenerationPanel.add(deathLimitField);
		
		mapGenerationPanel.setBorder(new TitledBorder("Map generation"));
		
		JPanel extraCellsPanel = new JPanel(new MigLayout());
		
		addSandCells = new JButton("Add sand cells");
		addSandCells.addActionListener(this);
		extraCellsPanel.add(addSandCells);
		
		extraCellsPanel.setBorder(new TitledBorder("Extra cells"));
		
		display = new Display(generator);
		display.setPreferredSize(new Dimension(600, 600));
		add(display, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel(new MigLayout());
		
		swapDisconnectedCavernsState = new JButton("Remove disconnected caverns");
		swapDisconnectedCavernsState.addActionListener(this);
		buttons.add(swapDisconnectedCavernsState);
		
		doStepButton = new JButton("Apply transition rules");
		doStepButton.addActionListener(this);
		buttons.add(doStepButton);

		generateCaveButton = new JButton("Generate new cave");
		generateCaveButton.addActionListener(this);
		buttons.add(generateCaveButton);
		
		add(mapGenerationPanel, BorderLayout.NORTH);
		add(extraCellsPanel, BorderLayout.NORTH);
		add(buttons, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getSource() == addSandCells) {
				String text = null;
				if (display.generator.borderCellsFilledWithSand) {
					text = "Add sand cells";

					display.generator.replaceCells(Cell.FLOOR, Cell.SAND);
				}
				else {
					text = "Remove sand cells";
					
					display.generator.fillBorderCellsWithSand();
				}
				addSandCells.setText(text);
			}
			else if (event.getSource() == swapDisconnectedCavernsState) {
				display.generator.swapDisconnectedCavernsState();
				
				String text = null;
				if (display.generator.removedCaverns) {
					text = "Restore disconnected caverns";
				}
				else {
					text = "Remove disconnected caverns";
					
					if (display.generator.borderCellsFilledWithSand) {
						display.generator.fillBorderCellsWithSand();
					}
				}
				swapDisconnectedCavernsState.setText(text);
			}
			else if (event.getSource() == doStepButton) {
				if (display.generator.borderCellsFilledWithSand) {
					display.generator.replaceCells(Cell.FLOOR, Cell.SAND);
					display.generator.doStep();
					display.generator.fillBorderCellsWithSand();
				}
				else {
					display.generator.doStep();
				}
				
				if (display.generator.removedCaverns) {
					swapDisconnectedCavernsState.setEnabled(false);
				}
			}
			else if (event.getSource() == generateCaveButton) {
				int w = Integer.parseInt(widthField.getText());
				int h = Integer.parseInt(heightField.getText());
				
				float frequency = Float.parseFloat(frequencyField.getText());
				int birthLimit = Integer.parseInt(birthLimitField.getText());
				int deathLimit = Integer.parseInt(deathLimitField.getText());
				
				display.setWorldGenerator(new WorldGenerator(w, h, frequency, birthLimit, deathLimit));
				
				swapDisconnectedCavernsState.setEnabled(true);
				seedField.setText(String.valueOf(display.generator.getSeed()));
			}

		} catch (NumberFormatException exception) {
			System.err.println("Error " + exception.getMessage().toLowerCase());
		}
		display.update();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                	ex.printStackTrace();
                }

                JFrame frame = new JFrame("World generator");
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new WorldGeneratorUI(new WorldGenerator(75, 75)));
                frame.setResizable(false);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
	}
}