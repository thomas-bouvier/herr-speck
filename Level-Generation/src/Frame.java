import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private Display display;
	
	private final JTextField sizeField, frequencyField, birthLimitField, deathLimitField, seedField;
	private final JButton removeDisconnectedCavernsButton, doStepButton, generateCaveButton;
	
	
	public Frame(Cave map) {
		super("Cellular Automata");
		
		setSize(1000, 1000);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel content = new JPanel(new BorderLayout());
		
		display = new Display(map);
		content.add(display, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel();
		
		JPanel sizeFieldPanel = new JPanel(new BorderLayout());
		sizeFieldPanel.add(new JLabel("Size"), BorderLayout.WEST);
		sizeField = new JTextField(3);
		sizeField.setText(String.valueOf(map.size));
		sizeFieldPanel.add(sizeField, BorderLayout.CENTER);
		buttons.add(sizeFieldPanel);
		
		JPanel frequencyFieldPanel = new JPanel(new BorderLayout());
		frequencyFieldPanel.add(new JLabel("Frequency"), BorderLayout.WEST);
		frequencyField = new JTextField(3);
		frequencyField.setText(String.valueOf(map.chanceToStartAlive));
		frequencyFieldPanel.add(frequencyField, BorderLayout.CENTER);
		buttons.add(frequencyFieldPanel);
	    
		JPanel birthFieldPanel = new JPanel(new BorderLayout());
		birthFieldPanel.add(new JLabel("Birth limit"), BorderLayout.WEST);
		birthLimitField = new JTextField(2);
		birthLimitField.setText(String.valueOf(map.birthLimit));
		birthFieldPanel.add(birthLimitField, BorderLayout.CENTER);
		buttons.add(birthFieldPanel);
		
		JPanel deathFieldPanel = new JPanel(new BorderLayout());
		deathFieldPanel.add(new JLabel("Death limit"), BorderLayout.WEST);
		deathLimitField = new JTextField(2);
		deathLimitField.setText(String.valueOf(map.deathLimit));
		deathFieldPanel.add(deathLimitField, BorderLayout.CENTER);
		buttons.add(deathFieldPanel);
		
		JPanel seedFieldPanel = new JPanel(new BorderLayout());
		seedFieldPanel.add(new JLabel("Seed"), BorderLayout.WEST);
		seedField = new JTextField(13);
		seedField.setText(String.valueOf(map.seed));
		seedFieldPanel.add(seedField, BorderLayout.CENTER);
		buttons.add(seedFieldPanel);
		
		removeDisconnectedCavernsButton = new JButton("Remove disconnected caverns");
		removeDisconnectedCavernsButton.addActionListener(this);
		buttons.add(removeDisconnectedCavernsButton);
		
		doStepButton = new JButton("doStep()");
		doStepButton.addActionListener(this);
		buttons.add(doStepButton);

		generateCaveButton = new JButton("Generate");
		generateCaveButton.addActionListener(this);
		buttons.add(generateCaveButton);
		
		content.add(buttons, BorderLayout.SOUTH);
		getContentPane().add(content);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		float frequency;
		int size, birthLimit, deathLimit;
		
		try {
			size = Integer.parseInt(sizeField.getText());
			birthLimit = Integer.parseInt(birthLimitField.getText());
			deathLimit = Integer.parseInt(deathLimitField.getText());
			frequency = Float.parseFloat(frequencyField.getText());
			
			if (event.getSource() == removeDisconnectedCavernsButton) {
				display.map.removeDisconnectedCaverns();
			}
			else if (event.getSource() == doStepButton) {
				display.map.doStep(birthLimit, deathLimit);
			}
			else if (event.getSource() == generateCaveButton) {
				display.setMap(new Cave(size));
				seedField.setText(String.valueOf(display.map.seed));
			}

		} catch (NumberFormatException exception) {
			System.err.println("Error " + exception.getMessage().toLowerCase());
		}
		display.update();
	}
	
	public static void main(String[] args) {
		new Frame(new Cave(100));
	}
}