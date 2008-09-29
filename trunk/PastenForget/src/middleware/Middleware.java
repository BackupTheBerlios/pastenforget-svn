package middleware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import queue.Queue;
import ui.UserInterface;
import ui.gui.GUI;
import download.Download;
import download.hoster.Hoster;
import download.hoster.Megaupload;
import download.hoster.Netload;
import download.hoster.Rapidshare;
import download.hoster.Uploaded;

/**
 * Schnittstelle zwischen GUI und Core. (Spaeter evt. auch TUI)
 * 
 * @author cpieloth
 */
public class Middleware {

	private File file = null;

	private File destination = null;

	private UserInterface ui;

	private Map<Integer, Queue> queues;

	public Middleware() {
		queues = new HashMap<Integer, Queue>();
		queues.put(Hoster.RAPIDSHARE.getKey(), new Queue());
		queues.put(Hoster.UPLOADED.getKey(), new Queue());
		queues.put(Hoster.NETLOAD.getKey(), new Queue());
		queues.put(Hoster.MEGAUPLOAD.getKey(), new Queue());
	}

	/**
	 * Startet einen Download.
	 * 
	 * @param url
	 */
	public String download(URL url) {
		if (!(url.equals("") || url.equals("Kein Link angegeben!"))) {
			Download download;
			switch (checkHoster(url.toString())) {
			case 0:
				download = new Rapidshare(url, queues.get(Hoster.RAPIDSHARE
						.getKey()));
				queues.get(Hoster.RAPIDSHARE.getKey()).addDownload(download);
				break;
			case 1:
				download = new Uploaded(url, queues.get(Hoster.UPLOADED
						.getKey()));
				queues.get(Hoster.UPLOADED.getKey()).addDownload(download);
				break;
			case 2:
				download = new Megaupload(url, queues.get(Hoster.MEGAUPLOAD
						.getKey()));
				queues.get(Hoster.MEGAUPLOAD.getKey()).addDownload(download);
				break;
			case 3:
				download = new Netload(url, queues.get(Hoster.NETLOAD.getKey()));
				queues.get(Hoster.NETLOAD.getKey()).addDownload(download);
				break;
			default:
				System.out.println("Unsupported Hoster");
				break;
			}
			System.out.println("Start download: " + url);
			return "Download gestartet!";
		} else {
			System.out.println("Start download: failure");
			return "Fehler!";
		}
	}

	/**
	 * Beginnt Stapelverarbeitung.
	 */
	public void load() {
		if (file != null) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				System.out.println("Start load: file not found");
				e.printStackTrace();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			List<String> links = new ArrayList<String>();
			try {
				while (br.ready()) {
					links.add(br.readLine());
				}
			} catch (IOException e) {
				System.out.println("Start load: IOException");
				e.printStackTrace();
			}

			for (String url : links) {
				try {
					download(new URL(url));
				} catch (MalformedURLException e) {
					System.out.println("Start load: wrong URL format");
					e.printStackTrace();
				}
				System.out.println(url);
			}
			
			System.out.println("Start load: " + file.getPath());
		} else {
			System.out.println("Start load: no file");
		}
	}

	public void cancel(Queue queue) {
		queue.removeCurrent();
	}

	private int checkHoster(String url) {
		if (url.indexOf(Hoster.RAPIDSHARE.getName()) != -1) {
			return Hoster.RAPIDSHARE.getKey();
		} else if (url.indexOf(Hoster.UPLOADED.getName()) != -1) {
			return Hoster.UPLOADED.getKey();
		} else if (url.indexOf(Hoster.MEGAUPLOAD.getName()) != -1) {
			return Hoster.MEGAUPLOAD.getKey();
		} else if (url.indexOf(Hoster.NETLOAD.getName()) != -1) {
			return Hoster.NETLOAD.getKey();
		} else {
			return Hoster.OTHER.getKey();
		}
	}

	public Queue getQueue(int hoster) {
		for (Hoster h : Hoster.values()) {
			if (h.getKey() == hoster) {
				return queues.get(h.getKey());
			}
		}
		return null;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		if (file != null) {
			System.out.println("Set file: " + file.getPath());
		} else {
			System.out.println("Set file: no file");
		}
	}

	public File getDestination() {
		return destination;
	}

	public void setDestination(File destination) {
		this.destination = destination;
		if (destination != null) {
			System.out.println("Set destination: " + destination.getPath());
		} else {
			System.out.println("Set destination: no destination");
		}
	}

	public UserInterface getUI() {
		return ui;
	}

	public void setUI(UserInterface ui) {
		this.ui = ui;
		System.out.println("Set UserInterface: done");
	}

	public void exit() {
		// TODO Downloads in Queues sichern
		// FIXME siehe GUI, Thread laufen weiter, bei Aufruf dieser Methode
		System.getSecurityManager().checkExit(0);
		System.exit(0);
	}

	/**
	 * Startet das Programm.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Middleware middleware = new Middleware();
		// TODO Gesicherte Downloads laden
		middleware.setUI(new GUI(middleware));
	}

}