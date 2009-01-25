package searchWebsite;


/**
 * Aufzaehlung aller Hoster und deren Nummerierung.
 * 
 * @author cpieloth
 * 
 */
public enum SearchWebsiteEnum {
	DDLWAREZ("ddl-warez.org", "http://ddl-warez[.]org/detail[.]php.*|http://www[.]ddl-warez[.]org/detail[.]php.*", "searchWebsite.DDLWarez");

	private final String NAME;
	
	private final String PATTERN;
	
	private final String CLASSNAME;

	SearchWebsiteEnum(String name, String pattern, String className) {
		this.NAME = name;
		this.PATTERN = pattern;
		this.CLASSNAME= className;
	}


	public String getName() {
		return NAME;
	}

	public String getPattern() {
		return PATTERN;
	}
	
	public String getClassName() {
		return CLASSNAME;
	}

}
