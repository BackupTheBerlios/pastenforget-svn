package ui.gui.settings;

import ui.gui.GUI;

/**
 * Aufzaehlung aller Settings.
 * 
 * @author executor
 * 
 */
public enum SettingsEnum {
	LOOKANDFEEL(0, "Look & Feel"), DIRECTORIES(1, "Verzeichnisse");

	private final int KEY;

	private final String LABEL;

	SettingsEnum(int key, String LABEL) {
		this.KEY = key;
		this.LABEL = LABEL;
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
		default:
			return null;
		}
	}

}
