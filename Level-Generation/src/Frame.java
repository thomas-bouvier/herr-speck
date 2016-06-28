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
	
	private final JTextField frequencyField, birthLimitField, deathLimitField;
	private final JButton generate, doStep;
	
	
	public Frame(Map map) {
		super("Cellular Automata");
		
		setSize(600, 600);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel content = new JPanel(new BorderLayout());
		
		display = new Display(map);
		content.add(display, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel();
		
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
		
		generate = new JButton("Generate");
		generate.addActionListener(this);
		buttons.add(generate);
		
		doStep = new JButton("doStep()");
		doStep.addActionListener(this);
		buttons.add(doStep);

		content.add(buttons, BorderLayout.SOUTH);
		getContentPane().add(content);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		float frequency;
		int birthLimit, deathLimit;
		try {
			birthLimit = Integer.parseInt(birthLimitField.getText());
			deathLimit = Integer.parseInt(deathLimitField.getText());
			frequency = Float.parseFloat(frequencyField.getText());
			if (event.getSource() == generate) {
				display.map.populate(frequency, birthLimit, deathLimit);
			} else if (event.getSource() == doStep) {
				display.map.doStep(birthLimit, deathLimit);
			}
		} catch (NumberFormatException exception) {
			System.err.println("Error " + exception.getMessage().toLowerCase());
		}
		display.update();
	}
	
	public static void main(String[] args) {
		new Frame(new Map(50, 50));
	}
}