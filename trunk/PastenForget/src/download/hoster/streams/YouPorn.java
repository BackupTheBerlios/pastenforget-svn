package download.hoster.streams;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import queue.Queue;

public class YouPorn extends Stream {
	public YouPorn(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.getUrl().toString());
		this.setHosterCaption("youporn");
	}

	public String createFileName() {
		String file = this.getUrl().getFile();
		String regex = "[^/]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(file);
		String filename = new String();
		while (m.find()) {
			filename = m.group();
		}

		return filename + ".flv";
	}
	/*
	@Override
	public void run() {
		try {
			this.setFileName(this.createFileName());
			URL url = this.getUrl();
			URLConnection urlc = url.openConnection();
			InputStream is = urlc.getInputStream();
			String page = Parser.convertStreamToString(is, false);
			String cookie = urlc.getHeaderFields().get("Set-Cookie").get(0);
			Map<String, String> requestHeader = new HashMap<String, String>();
			requestHeader.put("Set-Cookie", cookie);
			Request request = new Request();
			request.setAction(this.getUrl().toString());
			request.addParameter("user_choice", "Enter");
			request.setHeader(requestHeader);
			is = request.request();
			page = Parser.convertStreamToString(is, false);
			List<String> jsVar = Parser.getJavaScript("var", page);
			if (jsVar.size() > 0) {
				String link = jsVar.get(0);
				String[] splits = link.split("'");
				for (String split : splits) {
					if (split.indexOf("http") != -1) {
						this.setDirectUrl(new URL(split));
					}
				}
				if (!this.isCanceled() && !this.isStopped()) {
					if(ServerDownload.checkContentType(this).indexOf("xml") != -1) {
						url = this.getDirectUrl();
						is = url.openConnection().getInputStream();
						page = Parser.convertStreamToString(is, false);
						String location = Parser.getComplexTag("location", page).get(0);
						this.setDirectUrl(new URL(Parser.getTagContent("location", location).replace("amp;", "")));
					}
					ServerDownload.download(this);
				}
			} else {
				this.run();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
*/
}
