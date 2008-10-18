package download.hoster.filehoster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import middleware.Tools;
import parser.FormProperties;
import parser.Request;
import parser.Tag;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import download.Status;
import exception.CancelException;
import exception.ErrorPageException;
import exception.LinkNotFoundException;
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
		this.setCurrentSize(0);
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

	@Override
	public void run() {
		try {
			/*
			 * Fordert die erste Rapidshare-Seite an.
			 */
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			Tag htmlDocument = Tools.getTagFromInputStream(in, false);
			List<Tag> divs = htmlDocument.getComplexTag("div");
			for (Tag div : divs) {
				String classAttr = div.getAttribute("class");
				if ("klappbox".equals(classAttr)) {
					if (div.toString().indexOf("not be found") != -1) {
						throw new LinkNotFoundException();
					}
				}
			}

			/*
			 * Suche nach dem Formular, welches für den Klick auf den
			 * "Free-User" Button erforderlich ist.
			 */
			List<FormProperties> forms = htmlDocument.getFormulars();

			/*
			 * Wenn das Formular nicht gefunden werden kann, ist die gewählte
			 * Rapidshare-Seite nicht erreichbar.
			 * 
			 * if (forms.size() == 0) { throw new ErrorPageException(); } /*
			 * Alle Daten, welche für den Post-Request (i.e. Klick) erforderlich
			 * sind, werden gefiltert. D.h. Action und Request-Parmeter
			 */

			Request request = new Request(forms.get(0));

			/*
			 * Das Ausführen des Klicks kann zu 2 Resultaten führen:
			 * 
			 * 1. Eine Seite mit Wartezeit und Direktlink wird als Response
			 * übermittelt
			 * 
			 * 2. Wird bereits eine andere Datei mit der selben IP
			 * herunterladen, so wird eine Error-Seite übermittelt
			 */
			in = request.post();
			htmlDocument = Tools.getTagFromInputStream(in, false);

			/*
			 * Prüfung, ob es sich um eine Error-Seite handelt
			 */
			divs = htmlDocument.getComplexTag("div");
			for (Tag div : divs) {
				String classAttr = div.getAttribute("class");
				if ("klappbox".equals(classAttr)) {
					if (div.toString().indexOf("already downloading") != -1) {
						System.out.println("Error: IP lädt gerade");
						this.setStatus(Status.getNoSlot(++this.counter));
						Thread.sleep(10000);
						if (this.isStopped()) {
							throw new StopException();
						}
						if (this.isCanceled()) {
							throw new CancelException();
						}
						throw new ErrorPageException();
					}
					List<Tag> paragraphs = div.getComplexTag("p");
					for(Tag paragraph : paragraphs) {
						if(paragraph.toString().indexOf("try again in") != -1) {
							String regex = "[0-9]+";
							Pattern p = Pattern.compile(regex);
							Matcher m = p.matcher(paragraph.toString());
							String waitingTime = new String();
							while (m.find()) {
								waitingTime = m.group();
							}
							System.out.println("Wartezeit: " + waitingTime
									+ " Minuten");
							this.setStatus("Warten (" + waitingTime + " Min.)");
							for (int i = Integer.valueOf(waitingTime) * 60; i > 0; i--) {
								if (this.isStopped()) {
									throw new StopException();
								} else if (this.isCanceled()) {
									throw new CancelException();
								} else {
									this.setStatus(Status
											.getWaitMin((i / 60) + 1));
								}
								Thread.sleep(1000);
							}
							throw new ErrorPageException();
						}
					}
				}
			}
			this.counter = 0;
			/*
			 * Ermittlung des Direktlinks aus dem Quellcode
			 */
			List<Tag> inputs = htmlDocument.getSimpleTag("input");
			for (Tag input : inputs) {
				if (input.getAttribute("name") != null) {
					if (input.getAttribute("name").equals("mirror")) {
						String directLink = input.getAttribute("onclick")
								.replaceAll("[^a-zA-Z0-9-.:/_]*", "");
						this.setDirectUrl(new URL(directLink
								.substring(directLink.indexOf("http"))));
						break;
					}
				}
			}

			System.out.println("Direct-URL: " + this.getDirectUrl().toString());
			if (this.getDirectUrl() == null) {
				throw new ErrorPageException();
			}
			/*
			 * Ermittlung der Wartezeit
			 */
			int waitingTime = 0;
			List<Tag> vars = htmlDocument.getJavascript();
			for (Tag var : vars) {
				String current = var.toString();
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
			if (this.isStopped()) {
				throw new StopException();
			}
			if (this.isCanceled()) {
				throw new CancelException();
			}
			this.wait(waitingTime);
			ServerDownload.download(this);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException interrupted) {
			interrupted.printStackTrace();
		} catch (LinkNotFoundException linkNotFound) {
			System.out.println("Error: Rapidshare-Seite nicht vorhanden!");
			this.setStatus(Status.getError("Rapidshare Seite nicht vorhanden"));
			this.getQueue().removeDownload(this.getIndex());
		} catch (ErrorPageException errorPage) {
			this.run();
		} catch (StopException stopped) {
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException canceled) {
			System.out.println("Download canceled: " + this.getFileName());
		}
	}
}