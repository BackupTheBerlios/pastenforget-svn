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
		this.setFileName(createFilename());
	}

	private String createFilename() {
		String file = this.getUrl().getFile();
		String regex = "[^/]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(file);
		String filename = new String();
		while (m.find()) {
			filename = m.group();
		}
		System.out.println(filename);

		return filename;
	}

	@Override
	public void run() {
		try {
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(in, false);

			
			String requestForm = Parser.getComplexTag("form", page).get(0);
			String action = Parser.getAttribute("action", requestForm);

			Request request = Hoster.readRequestFormular(requestForm);
			request.setAction(action);

			in = request.request();
			page = Parser.convertStreamToString(in, false);
			List<String> headings = Parser.getComplexTag("h1", page);
			for (String current : headings) {
				if (Parser.getTagContent("h1", current).equals("Error")) {
					System.out.println("Slot belegt");
					this.setStatus("Slot belegt - Versuch: " + ++counter);
					for(int i = 0; (i < 10) && (this.isAlive()); i++) {
						Thread.sleep(1000);
					}
					this.run();
				}
			}

			this.setStatus("Slot verfügbar");

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

			this.wait(waitingTime);
			if (this.isAlive()) {
				ServerDownload.download(this);
			} else if(this.isStopped()) {
					System.out.println("Download stopped: " + this.getFileName());
			} else {
					System.out.println("Download canceled: " + this.getFileName());
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// Rapidshare rs = new Rapidshare(new
		// URL("http://rapidshare.com/files/147616972/heroes-302-xor.part1.rar"),
		// null);

		// rs.run();
	}

}