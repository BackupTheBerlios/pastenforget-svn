package ui.gui.settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import settings.Languages;
import ui.gui.GUI;

public class SetLanguage extends JPanel implements SettingsInterface {

	private static final long serialVersionUID = 5852791272907519487L;

	private static final String LABEL = SettingsEnum.LANGUAGE.getLabel();

	private JPanel panel, panelOut;

	private JLabel label;

	private Vector<String> languages = new Vector<String>();

	private JList list;

	public SetLanguage(GUI gui) {
		this.setLayout(new BorderLayout());
		init();
	}

	private void init() {
		panel = new JPanel();
		panel.setBorder(new TitledBorder(Languages.getTranslation("Language")));

		list = new JList();
		list.setBorder(new TitledBorder(""));
		list.setPreferredSize(new Dimension(140, 200));
		int index = 0, i = 0;
		for (String language : Languages.getLanguages()) {
			languages.add(language);
			if (language.equals(settings.Settings.getLanguage())) {
				index = i;
			}
			i++;
		}
		list.setListData(languages);
		list.setSelectedIndex(index);
		list.setEnabled(true);
		panel.add(list);

		panelOut = new JPanel();
		panelOut.add(panel);
		this.add(panelOut, BorderLayout.CENTER);

		label = new JLabel();
		label.setText(Languages.getTranslation("Notice") + ": "
				+ Languages.getTranslation("NoticeLanguage"));
		this.add(label, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	@Override
	public void accept() {
		settings.Settings.setLanguage(languages.get(list.getSelectedIndex()));
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
