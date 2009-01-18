package searchWebsite;


/**
 * Aufzaehlung aller Hoster und deren Nummerierung.
 * 
 * @author cpieloth
 * 
 */
public enum SearchWebsiteEnum {
	DDLWAREZ(0, "ddl-warez.org", "DDL-Warez", "*");

	private final int KEY;

	private final String URL;

	private final String NAME;
	
	private final String CLASSNAME;

	SearchWebsiteEnum(int key, String url, String name, String className) {
		this.KEY = key;
		this.URL = url;
		this.NAME = name;
		this.CLASSNAME= className;
	}

	public int getKey() {
		return KEY;
	}

	public String getUrl() {
		return URL;
	}

	public String getName() {
		return NAME;
	}

	public String getClassName() {
		return CLASSNAME;
	}

}
