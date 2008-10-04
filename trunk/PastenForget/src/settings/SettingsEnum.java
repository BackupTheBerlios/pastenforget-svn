package settings;

// TODO Enum der Settings hinzufügen?!

/**
 * Aufzaehlung aller Settings.
 * 
 * @author executor
 * 
 */
public enum SettingsEnum {
	SETTINGS(0, "Settings"), LOOKANDFEEL(1, "LookAndFeel"), DOWNLOADDIR(2,
			"DownloadDirectory"), DDLDIR(3,
			"DdlDirectory"),LANGUAGE(4, "Language");

	private final int KEY;

	private String NAME;

	SettingsEnum(int key, String name) {
		this.KEY = key;
		this.NAME = name;
	}

	public int getKey() {
		return KEY;
	}

	public String getName() {
		return NAME;
	}
}
