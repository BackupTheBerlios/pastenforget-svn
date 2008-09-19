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
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import core.Download;
import core.DownloadInterface;
import core.Queue;
import core.ServerDownload;

public class Uploaded extends Download implements DownloadInterface {

	public Uploaded(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		// TODO toString ueberdenken
		this.setFilename(getFilename(url.toString()));
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

	public String getDirectlink(List<String> htmllinks) {
		// TODO
		return null;
	}

	public String getFilename(String input) {
		// TODO
		return null;
	}

	public WebResponse getUploadedPageNo1(String input) throws SAXException,
			IOException {
		WebConversation conversation = new WebConversation();
		WebRequest request;
		WebResponse response;

		request = new GetMethodWebRequest(input);
		response = conversation.getResponse(request);

		return response;
	}

	public void out(String input) {
		System.out.println(input);
	}

	public void downloadFileFromHoster(String link) {
		String filename;
		WebResponse pageNo1;
		String directlink;
		String contentOfPageNo1;
		List<String> allhtmllinks = new ArrayList<String>();

		try {
			out("Übergebener Link: " + link);
			out("1. Anforderung Seite 1");
			pageNo1 = getUploadedPageNo1(link);
			out("--> Seitenstream erhalten");
			out("2. Konvertiere Stream zu String");
			contentOfPageNo1 = pageNo1.getText();
			out("3. Ermittle Dateiname");
			filename = getFilename(link);
			out("--> Dateiname: " + filename);
			out("4. Ermittle alle html Link von Seite 2");
			allhtmllinks = extractLinksFromHtml(contentOfPageNo1);
			out("5. Extrahiere die Mirrors der Rapidshare Server");
			directlink = getDirectlink(allhtmllinks);
			out("--> Direct Downloadlink: " + directlink);

			if (this.stopThread.isStopped() == false) {
				serverDownload = new ServerDownload(directlink, filename, this);
				serverDownload.run();
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