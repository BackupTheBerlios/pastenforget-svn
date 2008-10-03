package middleware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

	private Settings settings = null;

	private UserInterface ui = null;

	private Map<Integer, Queue> queues;

	private final String downloadBackUp = "pnf-downloads.pnf";

	public Middleware() {
		start();
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Queue getQueue(int hoster) {
		for (Hoster h : Hoster.values()) {
			if (h.getKey() == hoster) {
				return queues.get(h.getKey());
			}
		}
		return null;
	}

	public UserInterface getUI() {
		return ui;
	}

	public void setUI(UserInterface ui) {
		this.ui = ui;
	}

	/**
	 * Startet einen Download.
	 * 
	 * @param url
	 */
	public boolean download(URL url) {
		if (!(url.equals("") || url.equals("Kein Link angegeben!"))) {
			Download download;
			switch (checkHoster(url.toString())) {
			case 0:
				download = new Rapidshare(url, settings.getDestination(),
						queues.get(Hoster.RAPIDSHARE.getKey()));
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
			return true;
		} else {
			System.out.println("Start download: failure");
			return false;
		}
	}

	/**
	 * Beginnt Stapelverarbeitung.
	 */
	public boolean load(File file) {
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
			return true;
		} else {
			System.out.println("Start load: no file");
			return false;
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

	private boolean restoreDownloads() {		
		return load(new File(downloadBackUp));
	}

	private boolean saveDownloads() {
		OutputStream ostream;
		OutputStreamWriter ostreamWriter;
		PrintWriter pWriter = null;
		
		try {
			ostream = new FileOutputStream(downloadBackUp);
			ostreamWriter = new OutputStreamWriter(ostream, "UTF-8");
			pWriter = new PrintWriter(ostreamWriter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		for (Queue queue : queues.values()) {
			for (Download download : queue.getQueue()) {
				pWriter.println(download.getUrl().toString());
			}
		}
		pWriter.println("http://pastenforget.backup");
		
		pWriter.close();
		return true;
	}

	public boolean start() {
		this.settings = new Settings();
		this.queues = new HashMap<Integer, Queue>();
		this.queues.put(Hoster.RAPIDSHARE.getKey(), new Queue());
		this.queues.put(Hoster.UPLOADED.getKey(), new Queue());
		this.queues.put(Hoster.NETLOAD.getKey(), new Queue());
		this.queues.put(Hoster.MEGAUPLOAD.getKey(), new Queue());

		if (settings.getUserInterface() > 0) {
			this.setUI(new GUI(this));
			System.out.println("Set UserInterface: done");
		} else {
			System.out.println("Set UserInterface: console are not supported");
		}

		this.restoreDownloads();
		return true;
	}

	public void exit() {
		this.saveDownloads();
		System.exit(0);
	}

	/**
	 * Startet das Programm.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Middleware();
	}

}