package ui.gui.settings;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import settings.Languages;
import settings.LookAndFeelEnum;
import ui.gui.GUI;

public class SetLookAndFeel extends JPanel implements SettingsInterface {

	private static final long serialVersionUID = 5852791272907519487L;

	private static final String LABEL = SettingsEnum.LOOKANDFEEL.getLabel();

	private GUI gui;

	private JPanel panel;

	private List<JRadioButton> buttons = new LinkedList<JRadioButton>();

	public SetLookAndFeel(GUI gui) {
		this.gui = gui;
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		init();
	}

	private void init() {
		panel = new JPanel();
		panel.setBorder(new TitledBorder(Languages
				.getTranslation("lookandfeel")));

		int i = 0;
		for (@SuppressWarnings("unused")
		LookAndFeelEnum laf : LookAndFeelEnum.values()) {
			i++;
		}
		panel.setLayout(new GridLayout(i / 2, 2));

		JRadioButton button;
		ButtonGroup group = new ButtonGroup();
		for (LookAndFeelEnum laf : LookAndFeelEnum.values()) {
			button = new JRadioButton(Languages.getTranslation(laf.getLabel()));
			if (laf.getKey() == 1) {
				button.setEnabled(false);
			}
			buttons.add(button);
			group.add(button);
			panel.add(button);
		}

		buttons.get(settings.Settings.getUserInterface()).setSelected(true);

		panel.setVisible(true);
		this.add(panel);
	}

	@Override
	public void accept() {
		short i = 0;
		for (JRadioButton button : buttons) {
			if (button.isSelected()) {
				settings.Settings.setUserInterface(i);
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
