package download;

/**
 * Aufzaehlung aller Hoster.
 * 
 * @author executor
 * 
 */
public enum HosterEnum {
	RAPIDSHARE(
			"Rapidshare","http://rapidshare[.]com/files/[0-9]+/.*|http://www[.]rapidshare[.]com/files/[0-9]+/.*",
			"download.hoster.Rapidshare"), UPLOADED("Uploaded", "Uploaded",
			"download.hoster.Uploaded"), MEGAUPLOAD("Megaupload",
			"Megaupload", "download.hoster.Megaupload"), IRC("IRC (Testing)",
			"IRC (alpha)", "download.irc.*"), YOUTUBE("Youtube", "YouTube",
			"download.streams.YouTube"), YOUPORN("Youporn", "YouPorn",
			"download.streams.YouPorn"), PORNHUB("Pornhub", "PornHub",
			"download.streams.PornHub"), REDTUBE("Redtube", "RedTube",
			"download.streams.RedTube"), OTHER("other", "Other", "other");

	private final String NAME;

	private final String PATTERN;

	private final String CLASSNAME;

	HosterEnum(String name, String pattern, String className) {
		this.NAME = name;
		this.PATTERN = pattern;
		this.CLASSNAME = className;
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
