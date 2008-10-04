package settings;

/**
 * Aufzaehlung aller Sprachen.
 * 
 * @author executor
 * 
 */
public enum LanguageEnum {
	STANDARD(0, "Standard (Deutsch)"), ENGLISH(1, "Englisch"), GERMAN(2,
			"Deutsch"), FRANCE(3, "Fanzösisch"), SPANISH(4, "Spanisch");

	private final int KEY;

	private final String LABEL;

	LanguageEnum(int key, String label) {
		this.KEY = key;
		this.LABEL = label;
	}

	public int getKey() {
		return KEY;
	}

	public String getLabel() {
		return LABEL;
	}

}
