package core.hoster;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import core.Download;
import core.DownloadInterface;
import core.Logger;
import core.Queue;
import core.ServerDownload;

/**
 * Extrahiert Directlinks und fuehrt den Download aus. Hoster: rapidshare.com
 * 
 * @author cschaedl
 */

public class Rapidshare extends Download implements DownloadInterface {

	public Rapidshare(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		// TODO toString ueberdenken
		this.setFilename(this.getFilename(url.toString()));
	}

	public List<String> extractLinksFromHtml(String input) {
		Pattern p;
		Matcher m;
		List<String> htmllinks = new ArrayList<String>();
		String regex;

		regex = "http://[\\w/{.}-]*";
		p = Pattern.compile(regex);
		m = p.matcher(input);
		while (m.find()) {
			String url = input.substring(m.start(), m.end());
			htmllinks.add(url);
		}

		return htmllinks;
	}

	public List<String> getRapidshareMirrorlinks(List<String> htmllinks) {
		List<String> mirrorlinks = new ArrayList<String>();
		String url;

		for (int i = 0; i < htmllinks.size(); i++) {
			url = htmllinks.get(i);
			if (url.indexOf("/files/") > 0) {
				if (url.indexOf("rs") > 0) {
					mirrorlinks.add(url);
				}
			}
		}

		return mirrorlinks;
	}

	public String getFilename(String input) {
		Pattern p;
		Matcher m;
		String filename = new String();
		String regex = "/";

		p = Pattern.compile(regex);
		m = p.matcher(input);
		while (m.find()) {
			filename = input.substring(m.start() + 1);
		}

		return filename;
	}

	public String getWaitingTime(String input) {
		Pattern p;
		Matcher m;
		String waitingTime = new String();
		String regex = "var c=[0-9]*";

		p = Pattern.compile(regex);
		m = p.matcher(input);

		while (m.find()) {
			waitingTime = input.substring(m.start(), m.end()).replace("var c=",
					"");
		}

		return waitingTime;
	}

	public int wait(String waitingtime) {
		String runningtime;
		Integer time;
		time = Integer.valueOf(waitingtime);
		try {
			for (int i = time; i > 0; i--) {
				if (this.stopThread.isStopped() == true) {
					return 1;
				}
				Thread.sleep(1000);
				runningtime = String.valueOf(i);
				this.setStatus(runningtime + " Sek.");
				System.out.print(runningtime + " ");
			}
		} catch (InterruptedException ite) {
			System.out.println("Wartevorgang unterbrochen");
		}
		return 0;
	}

	public WebResponse getRapidsharePageNo1(String input) throws SAXException,
			IOException {
		WebConversation conversation = new WebConversation();
		WebRequest request;
		WebResponse response;

		request = new GetMethodWebRequest(input);
		response = conversation.getResponse(request);

		return response;
	}

	public WebResponse getRapidsharePageNo2(WebResponse input)
			throws SAXException, IOException {
		WebForm freeuser;
		WebResponse response;

		freeuser = input.getFormWithID("ff");
		response = freeuser.submit();

		return response;
	}

	public void downloadFileFromHoster(String link) {
		Integer status;
		String waitingtime, filename;
		WebResponse pageNo1, pageNo2;
		String directlink;
		String contentOfPageNo2;
		List<String> allhtmllinks = new ArrayList<String>();
		List<String> mirrors = new ArrayList<String>();
		Logger logger = null;
		try {
			logger = new Logger("C:\\fhd.log");
		} catch (Exception e) {
		}
		try {
			logger.log("Übergebener Link: " + link);
			logger.log("1. Anforderung Seite 1");
			pageNo1 = getRapidsharePageNo1(link);
			logger.log("--> Seitenstream erhalten");
			logger.log("2. Ermittle Link für Seite 2");
			logger
					.log("3. Anforderung Seite 2 mit Parametern (\"dl.start\", \"Free\")");
			pageNo2 = getRapidsharePageNo2(pageNo1);
			logger.log("--> Seitenstream erhalten");
			logger.log("4. Konvertiere Stream zu String");
			contentOfPageNo2 = pageNo2.getText();
			logger.log("5. Ermittle Dateiname");
			filename = getFilename(link);
			logger.log("--> Dateiname: " + filename);
			logger.log("6. Ermittle Wartezeit");
			waitingtime = getWaitingTime(contentOfPageNo2);
			logger.log("--> Wartezeit: " + waitingtime + " Sekunden");
			logger.log("7. Ermittle alle html Link von Seite 2");
			allhtmllinks = extractLinksFromHtml(contentOfPageNo2);
			logger.log("8. Extrahiere die Mirrors der Rapidshare Server");
			mirrors = getRapidshareMirrorlinks(allhtmllinks);
			directlink = mirrors.get(1); // Server: unknown
			logger.log("--> Direct Downloadlink: " + directlink);
			logger.log("9. Warte auf Ende des Download Limit");
			status = wait(waitingtime);
			if (status == 0) {
				this.serverDownload = new ServerDownload(directlink, filename,
						this);
				this.serverDownload.run();
			}
			this.stop();

		} catch (IOException ioex) {
			System.out.println("Rapidshare Seite nicht erreichbar");
		} catch (SAXException saex) {
			System.out
					.println("HTML Quelltextänderung im Seitenaufbau von Rapidshare");
		} catch (Exception ex) {
			System.out.println("Fehler in der Programmausführung");
		}
	}

}