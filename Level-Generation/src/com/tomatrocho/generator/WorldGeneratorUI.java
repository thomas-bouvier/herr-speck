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
	
	private final JButton doStepButton;
	
	private final JButton addSandCellsButton, placeEntranceButton;
	
	private final JButton swapDisconnectedCavernsState;
	
	private final JButton generateCaveButton, generateCaveRandomSeedButton;
	
	
	public WorldGeneratorUI(WorldGenerator generator) {
		setSize(1000, 1000);
		setLayout(new MigLayout());
		
		// map properties panel
		
		JPanel mapPropertiesPanel = new JPanel(new MigLayout());
		
		JLabel widthLabel = new JLabel("Width");
		widthField = new JTextField(3);
		widthField.setText(String.valueOf(generator.getW()));
		mapPropertiesPanel.add(widthLabel);
		mapPropertiesPanel.add(widthField);
		
		JLabel heightLabel = new JLabel("Height");
		heightField = new JTextField(3);
		heightField.setText(String.valueOf(generator.getH()));
		mapPropertiesPanel.add(heightLabel);
		mapPropertiesPanel.add(heightField);
		
		JLabel seedLabel = new JLabel("Seed");
		seedField = new JTextField(15);
		seedField.setText(String.valueOf(generator.getSeed()));
		mapPropertiesPanel.add(seedLabel, "gap unrelated");
		mapPropertiesPanel.add(seedField);
		
		mapPropertiesPanel.setBorder(new TitledBorder("Map properties"));
		
		// cellular automata panel
		
		JPanel cellularAutomataPanel = new JPanel(new MigLayout());
		
		JLabel frequencyLabel = new JLabel("Frequency");
		frequencyField = new JTextField(3);
		frequencyField.setText(String.valueOf(generator.getFrequency()));
		cellularAutomataPanel.add(frequencyLabel, "gap unrelated");
		cellularAutomataPanel.add(frequencyField);
	    
		JLabel birtLimitLabel = new JLabel("Birth threshold");
		birthLimitField = new JTextField(1);
		birthLimitField.setText(String.valueOf(generator.getBirthThreshold()));
		cellularAutomataPanel.add(birtLimitLabel, "gap unrelated");
		cellularAutomataPanel.add(birthLimitField);
		
		JLabel deathLimitLabel = new JLabel("Death threshold");
		deathLimitField = new JTextField(1);
		deathLimitField.setText(String.valueOf(generator.getDeathThreshold()));
		cellularAutomataPanel.add(deathLimitLabel, "gap unrelated");
		cellularAutomataPanel.add(deathLimitField);
		
		doStepButton = new JButton("Apply transition rules");
		doStepButton.addActionListener(this);
		cellularAutomataPanel.add(doStepButton, "gapleft 20");
		
		cellularAutomataPanel.setBorder(new TitledBorder("Cellular automata"));
		
		// extra cells panel
		
		JPanel extraCellsPanel = new JPanel(new MigLayout());
		
		addSandCellsButton = new JButton("Add sand cells");
		addSandCellsButton.addActionListener(this);
		extraCellsPanel.add(addSandCellsButton);
		
		placeEntranceButton = new JButton("Place entrance");
		placeEntranceButton.addActionListener(this);
		placeEntranceButton.setEnabled(false);
		extraCellsPanel.add(placeEntranceButton);

		extraCellsPanel.setBorder(new TitledBorder("Extra cells"));
		
		// caverns panel
		
		JPanel cavernsPanel = new JPanel(new MigLayout());
		
		swapDisconnectedCavernsState = new JButton("Remove disconnected caverns");
		swapDisconnectedCavernsState.addActionListener(this);
		swapDisconnectedCavernsState.setEnabled(false);
		cavernsPanel.add(swapDisconnectedCavernsState);
		
		cavernsPanel.setBorder(new TitledBorder("Caverns"));
		
		// display panel
		
		display = new Display(generator);
		display.setPreferredSize(new Dimension(600, 600));
		seedField.setText(String.valueOf(generator.getSeed()));
		
		// buttons panel
		
		JPanel buttons = new JPanel(new MigLayout());

		generateCaveButton = new JButton("Generate new cave with current seed");
		generateCaveButton.addActionListener(this);
		buttons.add(generateCaveButton);
		
		generateCaveRandomSeedButton = new JButton("Generate new cave with random seed");
		generateCaveRandomSeedButton.addActionListener(this);
		buttons.add(generateCaveRandomSeedButton);
		
		add(mapPropertiesPanel, "split 2");
		add(cellularAutomataPanel, "span 1 2, growy, wrap");
		add(extraCellsPanel, "split 2, growx");
		add(cavernsPanel, "growx, wrap");
		add(display, "align center, wrap");
		add(buttons, "align center");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			if (event.getSource() == addSandCellsButton) {
				String text = null;
				if (display.generator.borderCellsFilledWithSand) {
					text = "Add sand cells";

					display.generator.replaceCells(Cell.FLOOR, Cell.SAND);
				}
				else {
					text = "Remove sand cells";
					
					display.generator.fillBorderCellsWithSand();
				}
				addSandCellsButton.setText(text);
			}
			else if (event.getSource() == placeEntranceButton) {
				display.generator.placeEntrance();
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
				placeEntranceButton.setEnabled(true);
				swapDisconnectedCavernsState.setEnabled(true);
				
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
				placeEntranceButton.setEnabled(false);
				swapDisconnectedCavernsState.setEnabled(false);
				
				int w = Integer.parseInt(widthField.getText());
				int h = Integer.parseInt(heightField.getText());
				long seed = Long.parseLong(seedField.getText());
				float frequency = Float.parseFloat(frequencyField.getText());
				int birthLimit = Integer.parseInt(birthLimitField.getText());
				int deathLimit = Integer.parseInt(deathLimitField.getText());
				
				display.setWorldGenerator(new WorldGenerator(w, h, seed, frequency, birthLimit, deathLimit));
				
				seedField.setText(String.valueOf(display.generator.getSeed()));
			}
			else if (event.getSource() == generateCaveRandomSeedButton) {
				placeEntranceButton.setEnabled(false);
				swapDisconnectedCavernsState.setEnabled(false);
				
				int w = Integer.parseInt(widthField.getText());
				int h = Integer.parseInt(heightField.getText());
				float frequency = Float.parseFloat(frequencyField.getText());
				int birthLimit = Integer.parseInt(birthLimitField.getText());
				int deathLimit = Integer.parseInt(deathLimitField.getText());
				
				display.setWorldGenerator(new WorldGenerator(w, h, frequency, birthLimit, deathLimit));
				
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