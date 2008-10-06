package download.hoster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Parser;
import parser.Request;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import exception.CancelException;
import exception.StopException;

/**
 * Extrahiert Directlinks und fuehrt den Download aus. Hoster: rapidshare.com
 * 
 * @author cschaedl
 */

public class Rapidshare extends Download {
	private int counter = 0;

	public Rapidshare(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		String fileName = this.createFilename(this.getUrl());
		this.setFileName(fileName);
	}

	/**
	 * Filtert aus einer gegebenen URL den Filenamen
	 * 
	 * @param url
	 * @return String, welcher den Dateinamen repräsentiert.
	 */
	private String createFilename(URL url) {
		String urlAsString = url.toString();
		String regex = "[^/]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(urlAsString);
		String fileName = new String();
		while (m.find()) {
			fileName = m.group();
		}
		return fileName;
	}

	/**
	 * Führt alle nötigen Schritte durch, die für den Download einer Datei von
	 * Rapidshare notwendig sind.
	 */
	@Override
	public void run() {
		try {
			/*
			 * Fordert die erste Rapidshare-Seite an.
			 */
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(in, false);

			/*
			 * Suche nach dem Formular, welches für den Klick auf den
			 * "Free-User" Button erforderlich ist.
			 */
			List<String> forms = Parser.getComplexTag("form", page);

			/*
			 * Wenn das Formular nicht gefunden werden kann, ist die gewählte
			 * Rapidshare-Seite nicht erreichbar.
			 */
			if (forms.size() == 0) {
				System.out.println("Error: Rapidshare Seite nicht erreichbar");
				this.cancel();
				throw new CancelException();
			}

			/*
			 * Alle Daten, welche für den Post-Request (i.e. Klick) erforderlich
			 * sind, werden gefiltert. D.h. Action und Request-Parmeter
			 */
			String requestForm = forms.get(0);
			String action = Parser.getAttribute("action", requestForm);

			Request request = Parser.readRequestFormular(requestForm);
			request.setAction(action);

			/*
			 * Das Ausführen des Klicks kann zu 2 Resultaten führen:
			 * 
			 * 1. Eine Seite mit Wartezeit und Direktlink wird als Response
			 * übermittelt
			 * 
			 * 2. Wird bereits eine andere Datei mit der selben IP
			 * herunterladen, so wird eine Error-Seite übermittelt
			 */
			in = request.request();
			page = Parser.convertStreamToString(in, false);

			/*
			 * Prüfung, ob es sich um eine Error-Seite handelt
			 */
			List<String> headings = Parser.getComplexTag("h1", page);
			for (String current : headings) {
				if (Parser.getTagContent("h1", current).equals("Error")) {
					System.out.println("Slot belegt");
					this.setStatus("Slot belegt - Versuch: " + ++counter);
					for (int i = 0; (i < 10); i++) {
						/*
						 * Sollte das stop oder cancel-Flag gesetzt sein, so
						 * wird eine Exception geworfen, welches den Prozess
						 * beendet.
						 */
						this.isStopped();
						this.isCanceled();
						Thread.sleep(1000);
					}
					this.run();
				}
			}

			/*
			 * Ermittlung des Direktlinks aus dem Quellcode
			 */
			List<String> inputs = Parser.getSimpleTag("input", page);
			Iterator<String> inputIt = inputs.iterator();
			while (inputIt.hasNext()) {
				String current = inputIt.next();
				if (Parser.getAttribute("name", current) != null) {
					if (Parser.getAttribute("name", current).equals("mirror")) {
						String directLink = Parser.getAttribute("onclick",
								current).replaceAll("[^a-zA-Z0-9-.:/_]*", "");
						this.setDirectUrl(new URL(directLink
								.substring(directLink.indexOf("http"))));
						break;
					}
				}
			}

			/*
			 * Ermittlung der Wartezeit
			 */
			int waitingTime = 0;
			List<String> vars = Parser.getJavaScript("var", page);
			Iterator<String> it = vars.iterator();
			while (it.hasNext()) {
				String current = it.next();
				if (current.matches(".*=[0-9\\s]+")) {
					current = current.replaceAll("[^0-9]+", "");
					waitingTime = new Integer(current);
					break;
				}
			}

			/*
			 * Sollte das stop oder cancel-Flag gesetzt sein, so wird eine
			 * Exception geworfen, welche den Prozess beendet.
			 */
			this.isStopped();
			this.isCanceled();
			this.wait(waitingTime);
			ServerDownload.download(this);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (StopException se) {
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException ce) {
			System.out.println("Download canceled: " + this.getFileName());
		}
	}
}