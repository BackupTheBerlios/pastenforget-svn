package download.hoster;

/**
 * Aufzaehlung aller Hoster und deren Nummerierung.
 * 
 * @author cpieloth
 * 
 */
public enum Hoster {
	OTHER(-1, "other"), 
	RAPIDSHARE(0, "rapidshare"), 
	UPLOADED(1, "uploaded"), 
	MEGAUPLOAD(2, "megaupload"), 
	NETLOAD(3, "netload"),
	FILEFACTORY(4, "netload");

	private final int key;

	private final String name;

	Hoster(int key, String name) {
		this.key = key;
		this.name = name;
	}

	public int getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

}
