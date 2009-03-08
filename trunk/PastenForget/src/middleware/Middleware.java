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

	private static UserInterface userInterface = null;

	private static Map<String, Queue> queues;

	public static Queue getQueue(String hoster) {
		return queues.get(hoster);
	}

	public static UserInterface getUI() {
		return userInterface;
	}

	public static void setUI(UserInterface ui) {
		userInterface = ui;
	}

	/**
	 * Lädt alle notwendigen Daten um das Programm zu starten.
	 * @return
	 */
	public static boolean start() {
		settings.Settings.restore();
		Languages.setLanguage(settings.Settings.getLanguage());
		Languages.restore();

		queues = new HashMap<String, Queue>();
		for (HosterEnum hoster : HosterEnum.values()) {
			queues.put(hoster.getName(), new Queue());
		}
		
		DownloadTools.restoreDownloads(DownloadTools.downloadFile);

		if (settings.Settings.getUserInterface() != 1) {
			setUI(new GUI());
			System.out.println("Middleware.start: set UI done");
		} else {
			System.out.println("Middleware.start: console are not supported");
		}
		
		return true;
	}

	/**
	 * Sichert alle notwendigen Daten und beendet das Programm.
	 */
	public static void exit() {
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
		start();
	}

}