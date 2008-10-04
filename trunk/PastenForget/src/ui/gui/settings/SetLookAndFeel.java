package ui.gui.settings;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ui.gui.GUI;

public class SetLookAndFeel extends JPanel implements SettingsInterface {

	private static final long serialVersionUID = 5852791272907519487L;
	
	private static final String LABEL = "Look & Feel";

	private GUI gui;
	
	private middleware.Settings settings;

	private final static String[] NAMES = { "Konsole", "Betriebssystem", "Metal", "Motif",
			"GTK" };

	private JRadioButton[] buttons;

	public SetLookAndFeel(GUI gui) {
		this.gui = gui;
		settings = this.gui.getMiddleware().getSettings();
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(NAMES.length, 1));

		buttons = new JRadioButton[NAMES.length];
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JRadioButton(NAMES[i]);
			if (i==0) {
				buttons[i].setEnabled(false);
			}
			group.add(buttons[i]);
			panel.add(buttons[i]);
		}
		
		buttons[settings.getUserInterface()].setSelected(true);

		panel.setVisible(true);
		this.add(panel);
	}

	@Override
	public void accept() {
		for (short i = 0; i < buttons.length; i++) {
			if (buttons[i].isSelected()) {
				settings.setUserInterface(i);
				this.gui.setLookAndFeel(i);
			}
		}
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

}
