package ui.gui.settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import settings.Languages;
import ui.gui.GUI;

public class SetLanguage extends JPanel implements SettingsInterface {

	private static final long serialVersionUID = 5852791272907519487L;

	private static final String LABEL = SettingsEnum.LANGUAGE.getLabel();

	private GUI gui;
	
	private JPanel panel;

	private settings.Settings settings;

	private Vector<String> languages = new Vector<String>();

	private JList list;

	public SetLanguage(GUI gui) {
		this.gui = gui;
		settings = this.gui.getMiddleware().getSettings();
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		init();
	}
	
	private void init() {
		panel = new JPanel();
		panel.setBorder(new TitledBorder(Languages.getTranslation("language")));

		list = new JList();
		list.setBorder(new TitledBorder(""));
		list.setPreferredSize(new Dimension(240, 200));
		int index = 0, i = 0;
		for (String language : Languages.getLanguages()) {
			languages.add(language);
			if (language.equals(this.settings.getLanguage())) {
				index = i;
			}
			i++;
		}
		list.setListData(languages);
		list.setSelectedIndex(index);
		list.setEnabled(true);

		panel.add(list);

		this.add(panel);
		this.setVisible(true);
	}

	@Override
	public void accept() {
		settings.setLanguage(languages.get(list.getSelectedIndex()));
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
