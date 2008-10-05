package download.hoster;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Parser;
import parser.Request;
import queue.Queue;
import stream.ServerDownload;
import download.Download;

public class YouPorn extends Download {
	public YouPorn(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.createFilename() + ".flv");
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

		return filename;
	}

	@Override
	public void run() {
		try {
			URL url = this.getUrl();
			URLConnection urlc = url.openConnection();
			InputStream is = urlc.getInputStream();
			String page = Parser.convertStreamToString(is, false);
			String cookie = urlc.getHeaderFields().get("Set-Cookie").get(0);
			Map<String, String> requestHeader = new HashMap<String, String>();
			requestHeader.put("Set-Cookie", cookie);
			String requestForm = Parser.getComplexTag("form", page).get(0);
			Request request = new Request();
			request.setAction(this.getUrl().toString());
			List<String> input = Parser.getSimpleTag("input", requestForm);
			Iterator<String> inputIt = input.iterator();
			while (inputIt.hasNext()) {
				String currentInput = inputIt.next();
				String name = new String();
				String value = new String();
				if ((name = Parser.getAttribute("name", currentInput)) != null) {
					value = Parser.getAttribute("value", currentInput);
					if (value.equals("Enter")) {
						request.addParameter(name, value);
					}
				}
			}
			request.setHeader(requestHeader);
			is = request.request();
			page = Parser.convertStreamToString(is, false);
			String link = Parser.getJavaScript("var", page).get(0);
			String[] splits = link.split("'");
			for (String split : splits) {
				if (split.indexOf("http") != -1) {
					this.setDirectUrl(new URL(split));
				}
			}

			if (this.isAlive()) {
				ServerDownload.download(this);
			} else {
				System.out.println("Download canceled: " + this.getFileName() );
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
