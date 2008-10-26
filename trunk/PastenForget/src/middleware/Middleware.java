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
import settings.Languages;
import ui.UserInterface;
import ui.gui.GUI;
import decrypt.RSDF;
import download.Download;
import download.hoster.HosterEnum;
import download.irc.IRCThread;
import filtration.RequestPackage;

/**
 * Schnittstelle zwischen GUI und Core. (Spaeter evt. auch TUI)
 * 
 * @author cpieloth
 */
public class Middleware {

	private UserInterface ui = null;

	private Map<Integer, Queue> queues;

	private final File downloadBackUp = new File(Tools.getProgramPath()
			.getAbsolutePath()
			+ "/pnf-downloads.pnf");

	public Middleware() {
		start();
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
	 * Startet einen IRC-Download.
	 * 
	 * @param RequestPackage
	 */
	public boolean downloadIrc(RequestPackage requestPackage) {
		if (requestPackage != null) {
			Download download = new IRCThread();
			download.setIrc(requestPackage);
			download.setInformation(null, settings.Settings.getDownloadDirectory(), queues.get(HosterEnum.IRC.getKey()));
			queues.get(HosterEnum.IRC.getKey()).addDownload(download);
			System.out.println("Add download: " + requestPackage.getPackage());
			return true;
		} else {
			System.out.println("Add download: failure");
			return false;
		}
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
					Class<?> myClass;
					try {
						myClass = Class.forName(hoster.getClassName());
						download = (Download) myClass.newInstance();
						download.setInformation(url, settings.Settings
								.getDownloadDirectory(), queues.get(hoster
								.getKey()));
						queues.get(hoster.getKey()).addDownload(download);
						System.out.println("Add download: " + url);
						return true;
					} catch (Exception e) {
						System.out.println("Add download: failure " + url);
						return false;
					}
				}
			}
			System.out.println("Add download: hoster not supported " + url);
			return false;
		} else {
			System.out.println("Add download: failure");
			return false;
		}
	}

	/**
	 * Liest eine pnf-Datei ein.
	 */
	public boolean loadPnf(File file) {
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

	/**
	 * Liest einen RSDF-Container ein.
	 */
	public boolean loadRsdf(File file) {
		if (file != null && file.exists()) {
			List<String> downloadList = RSDF.decodeRSDF(file);

			for (String url : downloadList) {
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
		if (downloadBackUp.exists()) {
			return loadPnf(downloadBackUp);
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
		settings.Settings.restore();
		Languages.setLanguage(settings.Settings.getLanguage());
		Languages.restore();
		this.queues = new HashMap<Integer, Queue>();

		for (HosterEnum hoster : HosterEnum.values()) {
			this.queues.put(hoster.getKey(), new Queue());
		}

		if (settings.Settings.getUserInterface() != 1) {
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
	 * @throws IOException
	 */
	public static void main(String[] args) {
		new Middleware();
	}

}