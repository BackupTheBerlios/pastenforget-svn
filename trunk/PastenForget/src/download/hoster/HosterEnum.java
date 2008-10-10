package download.hoster;

import java.io.File;
import java.net.URL;

import queue.Queue;
import download.Download;
import download.hoster.filehoster.FileFactory;
import download.hoster.filehoster.Megaupload;
import download.hoster.filehoster.Netload;
import download.hoster.filehoster.Rapidshare;
import download.hoster.filehoster.Uploaded;
import download.hoster.streams.PornHub;
import download.hoster.streams.RedTube;
import download.hoster.streams.YouPorn;
import download.hoster.streams.YouTube;

/**
 * Aufzaehlung aller Hoster und deren Nummerierung.
 * 
 * @author cpieloth
 * 
 */
public enum HosterEnum {
	OTHER(-1, "other", "Other"), RAPIDSHARE(0, "rapidshare", "Rapidshare"), UPLOADED(
			1, "uploaded", "Uploaded"), MEGAUPLOAD(2, "megaupload",
			"(Megaupload)"), NETLOAD(3, "netload", "(Netload)"), FILEFACTORY(4,
			"filefactory", "(FileFactory)"), PORNHUB(5, "pornhub", "PornHub"), YOUPORN(
			6, "youporn", "YouPorn"), YOUTUBE(7, "youtube", "YouTube"),  REDTUBE(8, "redtube", "RedTube");

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
			return new Uploaded(url, destination, queue);
		case 2:
			return new Megaupload(url, destination, queue);
		case 3:
			return new Netload(url, destination, queue);
		case 4:
			return new FileFactory(url, destination, queue);
		case 5:
			return new PornHub(url, destination, queue);
		case 6:
			return new YouPorn(url, destination, queue);
		case 7:
			return new YouTube(url, destination, queue);
		case 8:
			return new RedTube(url, destination, queue);
		default:
			return null;
		}
	}

}
