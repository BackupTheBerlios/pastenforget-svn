package ui.gui.settings;

import settings.Languages;
import ui.gui.GUI;

/**
 * Aufzaehlung aller Settings.
 * 
 * @author executor
 * 
 */
public enum SettingsEnum {
	LOOKANDFEEL(0, Languages.getTranslation("lookandfeel")), DIRECTORIES(1,
			Languages.getTranslation("folders")), LANGUAGE(2, Languages
			.getTranslation("language"));

	private final int KEY;

	private final String LABEL;

	SettingsEnum(int key, String label) {
		this.KEY = key;
		this.LABEL = label;
	}

	public int getKey() {
		return KEY;
	}

	public String getLabel() {
		return LABEL;
	}

	public SettingsInterface getSetting(GUI gui) {
		switch (KEY) {
		case 0:
			return new SetLookAndFeel(gui);
		case 1:
			return new SetDirectories(gui);
		case 2:
			return new SetLanguage(gui);
		default:
			return null;
		}
	}

}
