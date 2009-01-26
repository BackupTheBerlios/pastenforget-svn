package middleware;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import queue.Queue;
import settings.Languages;
import ui.UserInterface;
import ui.gui.GUI;
import download.DownloadTools;
import download.HosterEnum;

/**
 * Schnittstelle zwischen UI und Core.
 * 
 * @author executor
 */
public class Middleware {

	private UserInterface ui = null;

	private Map<String, Queue> queues;

	public Middleware() {
		start();
	}

	public Queue getQueue(String hoster) {
		return queues.get(hoster);
	}

	public UserInterface getUI() {
		return ui;
	}

	public void setUI(UserInterface ui) {
		this.ui = ui;
	}

	/**
	 * Lädt alle notwendigen Daten um das Programm zu starten.
	 * @return
	 */
	public boolean start() {
		settings.Settings.restore();
		Languages.setLanguage(settings.Settings.getLanguage());
		Languages.restore();
		DownloadTools.setMiddleware(this);
		DownloadTools.restoreDownloads(DownloadTools.downloadFile);

		this.queues = new HashMap<String, Queue>();
		for (HosterEnum hoster : HosterEnum.values()) {
			this.queues.put(hoster.getName(), new Queue());
		}

		if (settings.Settings.getUserInterface() != 1) {
			this.setUI(new GUI(this));
			System.out.println("Middleware.start: set UI done");
		} else {
			System.out.println("Middleware.start: console are not supported");
		}
		return true;
	}

	/**
	 * Sichert alle notwendigen Daten und beendet das Programm.
	 */
	public void exit() {
		DownloadTools.saveDownloads(DownloadTools.downloadFile);
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