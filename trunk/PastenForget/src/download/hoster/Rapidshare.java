package download.hoster;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Formular;
import parser.Tag;
import web.Connection;
import download.Download;
import download.DownloadThread;
import download.Status;

public class Rapidshare extends Download {
	public Rapidshare(URL url, File destination) {
		super(url, destination);
	}

	private int counter = 0;

	@Override
	public boolean cancel() {
		if(this.getThread() != null) {
			this.getThread().stop();
		}
		this.setStatus(Status.getCanceled());
		return true;
	}

	@Override
	public boolean start() {
		this.setStart(true);
		this.setThread(new DownloadThread(this));
		this.getThread().start();
		this.setStatus(Status.getStarted());
		return false;
	}

	@Override
	public boolean stop() {
		if(this.getThread() != null) {
			this.getThread().stop();
		}
		this.setStop(true);
		this.setStatus(Status.getStopped());
		return true;
	}

	public boolean restart() {
		this.getThread().stop();
		this.getThread().start();
		this.setStart(true);
		return true;
	}
	
	@Override
	public void run() {
		HosterUtilities util = new HosterUtilities(this);
		try {
			Connection webConnection = new Connection();
			webConnection.connect(this.getUrl());
			Tag document = webConnection.getDocument();
			List<Tag> divElements = document.getComplexTag("div");
			for (Tag div : divElements) {
				String cssClass = div.getAttribute("class");
				if ("klappbox".equals(cssClass)
						&& div.toString().indexOf("not be found") != -1) {
					System.out.println("Error: Link cannot be found");
					throw new ThreadDeath();
				}
			}

			Tag form = document.getComplexTag("form").get(0);
			Formular formular = new Formular(form);
			String action = formular.getAction();
			Map<String, String> postParameters = formular.getPostParameters();

			webConnection.connect(action);
			webConnection.doPost(postParameters);
			document = webConnection.getDocument();
			divElements = document.getComplexTag("div");
			for (Tag div : divElements) {
				String classAttr = div.getAttribute("class");
				if ("klappbox".equals(classAttr)) {
					if (div.toString().indexOf("already downloading") != -1) {
						System.out.println("Error: IP lädt gerade");
						this.setStatus(Status.getNoSlot(++this.counter));
						try {
							Thread.sleep(10000);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						this.restart();
					}
					List<Tag> paragraphs = div.getComplexTag("p");
					for (Tag paragraph : paragraphs) {
						if (paragraph.toString().indexOf("try again in") != -1) {
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
								this.setStatus(Status.getWaitMin((i / 60) + 1));
								try {
									Thread.sleep(1000);
								} catch (InterruptedException ie) {
									ie.printStackTrace();
								}
							}
							this.restart();
						}
					}
				}
			}

			this.counter = 0;
			/*
			 * Ermittlung des Direktlinks aus dem Quellcode
			 */

			String directLink = new String();
			List<Tag> inputs = document.getSimpleTag("input");
			for (Tag input : inputs) {
				if (input.getAttribute("name") != null) {
					if (input.getAttribute("name").equals("mirror")) {
						directLink = input.getAttribute("onclick").replaceAll(
								"[^a-zA-Z0-9-.:/_]*", "");
						directLink = directLink.substring(directLink
								.indexOf("http"));
						break;
					}
				}
			}
			System.out.println("Directlink: " + directLink);

			/*
			 * Ermittlung der Wartezeit
			 */
			int waitingTime = 0;
			List<Tag> variables = document.getJavascript();
			for (Tag variable : variables) {
				String current = variable.toString();
				if (current.matches(".*=[0-9\\s]+")) {
					current = current.replaceAll("[^0-9]+", "");
					waitingTime = new Integer(current);
					break;
				}
			}
			util.waitSeconds(waitingTime);
			util.download(directLink);
		} catch (IOException io) {
			io.printStackTrace();
		} catch (ThreadDeath td) {
			throw td;
		}
	}
}
