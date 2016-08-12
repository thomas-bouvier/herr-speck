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

public class CaveUI extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private Display display;
	
	private final JTextField sizeField, frequencyField, birthLimitField, deathLimitField, seedField;
	private final JButton swapDisconnectedCavernsState, doStepButton, generateCaveButton;
	
	
	public CaveUI(Cave map) {
		setSize(1000, 1000);
		setLayout(new MigLayout());
		
		TitledBorder titled = new TitledBorder("Map generation");
		
		JPanel fields = new JPanel(new MigLayout());
		
		JLabel sizeLabel = new JLabel("Size");
		sizeField = new JTextField(3);
		sizeField.setText(String.valueOf(map.size));
		fields.add(sizeLabel);
		fields.add(sizeField);
		
		JLabel seedLabel = new JLabel("Seed");
		seedField = new JTextField(15);
		seedField.setText(String.valueOf(map.seed));
		fields.add(seedLabel, "gap unrelated");
		fields.add(seedField);
		
		JLabel frequencyLabel = new JLabel("Frequency");
		frequencyField = new JTextField(3);
		frequencyField.setText(String.valueOf(map.chanceToStartAlive));
		fields.add(frequencyLabel, "gap unrelated");
		fields.add(frequencyField);
	    
		JLabel birtLimitLabel = new JLabel("Birth limit");
		birthLimitField = new JTextField(1);
		birthLimitField.setText(String.valueOf(map.birthLimit));
		fields.add(birtLimitLabel, "gap unrelated");
		fields.add(birthLimitField);
		
		JLabel deathLimitLabel = new JLabel("Death limit");
		deathLimitField = new JTextField(1);
		deathLimitField.setText(String.valueOf(map.deathLimit));
		fields.add(deathLimitLabel, "gap unrelated");
		fields.add(deathLimitField);
		
		fields.setBorder(titled);
		
		display = new Display(map);
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
		
		add(fields, BorderLayout.NORTH);
		add(buttons, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		float frequency;
		int size, birthLimit, deathLimit;
		
		try {
			size = Integer.parseInt(sizeField.getText());
			frequency = Float.parseFloat(frequencyField.getText());
			birthLimit = Integer.parseInt(birthLimitField.getText());
			deathLimit = Integer.parseInt(deathLimitField.getText());
			
			if (event.getSource() == swapDisconnectedCavernsState) {
				display.map.swapDisconnectedCavernsState();
				
				String text = null;
				if (display.map.removedCaverns) {
					text = "Restore disconnected caverns";
				} else {
					text = "Remove disconnected caverns";
				}
				swapDisconnectedCavernsState.setText(text);
			}
			else if (event.getSource() == doStepButton) {
				display.map.doStep();
				
				if (display.map.removedCaverns) {
					swapDisconnectedCavernsState.setEnabled(false);
				}
			}
			else if (event.getSource() == generateCaveButton) {
				display.setMap(new Cave(size, frequency, birthLimit, deathLimit));
				
				swapDisconnectedCavernsState.setEnabled(true);
				seedField.setText(String.valueOf(display.map.seed));
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
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                	ex.printStackTrace();
                }

                JFrame frame = new JFrame("Cave generator");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new CaveUI(new Cave(100)));
                frame.setResizable(false);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
	}
}