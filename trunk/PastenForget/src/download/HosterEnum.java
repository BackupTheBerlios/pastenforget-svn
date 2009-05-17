package download;

/**
 * Aufzaehlung aller Hoster.
 * 
 * @author executor
 * 
 */
public enum HosterEnum {
	// TODO blueHost
	RAPIDSHARE(
			"Rapidshare","http://rapidshare[.]com/files/[0-9]+/.*|http://www[.]rapidshare[.]com/files/[0-9]+/.*",
			"download.rapidshare.Rapidshare"), UPLOADED("Uploaded", "http://uploaded[.]to.*|http://www[.]uploaded[.]to.*",
			"download.uploaded.Uploaded"), NETLOAD("Netload", "http://www[.]netload[.]in.*|http://netload[.]in.*",
			"download.netload.Netload"), MEGAUPLOAD("Megaupload",
			"Megaupload", "download.megaupload.Megaupload"), IRC("IRC (unstable)",
			"IRC (alpha)", "download.irc.*"), YOUTUBE("Youtube", "YouTube",
			"download.streams.YouTube"), YOUPORN("Youporn", "YouPorn",
			"download.streams.YouPorn"), PORNHUB("Pornhub", "PornHub",
			"download.streams.PornHub"), REDTUBE("Redtube", "RedTube",
			"download.streams.RedTube"), SERIENJUNKIES("Serienjunkies", "http://download[.]serienjunkies[.]org.*", "download.hoster.Serienjunkies"),  OTHER("other", "Other", "other");

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
