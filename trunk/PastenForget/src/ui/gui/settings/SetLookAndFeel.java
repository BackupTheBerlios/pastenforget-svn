package ui.gui.settings;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import settings.LookAndFeelEnum;
import ui.gui.GUI;

public class SetLookAndFeel extends JPanel implements SettingsInterface {

	private static final long serialVersionUID = 5852791272907519487L;
	
	private static final String LABEL = SettingsEnum.LOOKANDFEEL.getLabel();

	private GUI gui;
	
	private settings.Settings settings;

	private final static String[] NAMES = { "Konsole", "Betriebssystem", "Metal", "Motif",
			"GTK" };

	private List<JRadioButton> buttons = new LinkedList<JRadioButton>();

	public SetLookAndFeel(GUI gui) {
		this.gui = gui;
		settings = this.gui.getMiddleware().getSettings();
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(NAMES.length, 1));

		JRadioButton button;
		ButtonGroup group = new ButtonGroup();
		for (LookAndFeelEnum laf : LookAndFeelEnum.values()) {
			button = new JRadioButton(laf.getLabel());
			if (laf.getKey() == 1) {
				button.setEnabled(false);
			}
			buttons.add(button);
			group.add(button);
			panel.add(button);
		}
		
		buttons.get(settings.getUserInterface()).setSelected(true);

		panel.setVisible(true);
		this.add(panel);
	}

	@Override
	public void accept() {
		short i = 0;
		for (JRadioButton button : buttons) {
			if (button.isSelected()) {
				settings.setUserInterface(i);
				this.gui.setLookAndFeel(i);
			}
			i++;
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
