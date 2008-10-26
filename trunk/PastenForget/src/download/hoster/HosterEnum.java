package download.hoster;


/**
 * Aufzaehlung aller Hoster und deren Nummerierung.
 * 
 * @author cpieloth
 * 
 */
public enum HosterEnum {
	OTHER(-1, "other", "Other", "other"),
	RAPIDSHARE(0, "rapidshare", "Rapidshare", "download.hoster.filehoster.Rapidshare"),
	UPLOADED(1, "uploaded", "Uploaded", "download.hoster.filehoster.Uploaded"),
	MEGAUPLOAD(2, "megaupload", "Megaupload", "download.hoster.filehoster.Megaupload"),
	IRC(3, "irc.irc", "IRC", "download.irc.*"),
	YOUTUBE(4, "youtube", "YouTube", "download.hoster.streams.YouTube"),
	YOUPORN(5, "youporn", "YouPorn", "download.hoster.streams.YouPorn"),
	PORNHUB(6, "pornhub", "PornHub", "download.hoster.streams.PornHub"),
	REDTUBE(7, "redtube", "RedTube", "download.hoster.streams.RedTube");

	private final int KEY;

	private final String URL;

	private final String NAME;
	
	private final String CLASSNAME;

	HosterEnum(int key, String url, String name, String className) {
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
