package download.hoster;

import java.io.File;
import java.net.URL;

import queue.Queue;
import download.Download;

/**
 * Aufzaehlung aller Hoster und deren Nummerierung.
 * 
 * @author cpieloth
 * 
 */
public enum HosterEnum {
	OTHER(-1, "other", "Other"), RAPIDSHARE(0, "rapidshare", "Rapidshare"), UPLOADED(
			1, "uploaded", "Uploaded"), MEGAUPLOAD(2, "megaupload",
			"Megaupload"), NETLOAD(3, "netload", "(Netload)"), FILEFACTORY(4,
			"filefactory", "(FileFactory)"), PORNHUB(5, "pornhub", "PornHub"), YOUPORN(
			6, "youporn", "YouPorn"), YOUTUBE(6, "youtube", "YouTube");

	private final int KEY;

	private final String URL;

	private final String NAME;

	HosterEnum(int key, String url, String name) {
		this.KEY = key;
		this.URL = url;
		this.NAME = name;
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

	public Download getDownload(URL url, File destination, Queue queue) {
		switch (KEY) {
		case -1:
			return null;
		case 0:
			return new Rapidshare(url, destination, queue);
		case 1:
			return new Uploaded(url, queue);
		case 2:
			return new Megaupload(url, queue);
		case 3:
			return new Netload(url, queue);
		case 4:
			return new FileFactory(url, queue);
		case 5:
			return new PornHub(url, queue);
		case 6:
			return new YouPorn(url, queue);
		case 7:
			return new YouTube(url, queue);
		default:
			return null;
		}
	}

}
