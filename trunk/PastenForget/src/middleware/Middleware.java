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
import settings.Settings;
import ui.UserInterface;
import ui.gui.GUI;
import download.Download;
import download.hoster.HosterEnum;

/**
 * Schnittstelle zwischen GUI und Core. (Spaeter evt. auch TUI)
 * 
 * @author cpieloth
 */
public class Middleware {

	private Settings settings = null;

	private UserInterface ui = null;

	private Map<Integer, Queue> queues;

	private final String downloadBackUp = Tools.getProgramPath() + "pnf-downloads.pnf";

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
		return queues.get(hoster);
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
			int h = Tools.checkHoster(url.toString());
			for (HosterEnum hoster : HosterEnum.values()) {
				if (h == hoster.getKey() && hoster.getKey() > -1) {
					download = hoster.getDownload(url, settings
							.getDownloadDirectory(), queues
							.get(hoster.getKey()));
					queues.get(hoster.getKey()).addDownload(download);
					break;
				}
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
			}

			System.out.println("Start load: " + file.getPath());
			return true;
		} else {
			System.out.println("Start load: no file");
			return false;
		}
	}

	private boolean restoreDownloads() {
		File backUp = new File(downloadBackUp);
		if (backUp.exists()) {
			return load(backUp);
		} else {
			return false;
		}
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
			for (Download download : queue.getDownloadList()) {
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

		for (HosterEnum hoster : HosterEnum.values()) {
			this.queues.put(hoster.getKey(), new Queue());
		}

		if (settings.getUserInterface() != 1) {
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