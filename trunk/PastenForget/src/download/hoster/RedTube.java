package download.hoster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import parser.Parser;
import queue.Queue;
import stream.ServerDownload;
import download.Download;
import exception.CancelException;
import exception.StopException;

public class RedTube extends Download {

	public RedTube(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(this.createFilename());
	}

	public String createFilename() {
		//try {
			this.setStatus("ermittle Filename");
			return "test";
		/*
			URL url = this.getUrl();
			InputStream is = url.openConnection().getInputStream();
			this.setStatus("ermittle Dateiname");
			String page = Parser.convertStreamToString(is, false);
			Parser.getComplexTag("title", page).get(0)
					.replace("RedTube - ", "").replaceAll("&[^;];", "");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	*/
	}

	@Override
	public void run() {
		try {

			this.setStatus("ermittle Directlink");
			String action = "http://www.redtubedownload.info/index.php?"
					+ "action=get_movie&ajax=true&url="
					+ URLEncoder.encode(this.getUrl().toString(), "UTF-8");
			URL url = new URL(action);
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, false);
			List<String> links = Parser.getSimpleTag("a", page);
			String targetLink = new String();
			for(String link : links ) {
				if(link.indexOf("redtube") != -1) {
					targetLink = link;
				}
			}
			System.out.println(Parser.getAttribute("href", targetLink));
			this.setDirectUrl(new URL(Parser.getAttribute("href", targetLink)));
			this.isCanceled();
			this.isStopped();
			ServerDownload.download(this);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (StopException se) {
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException ce) {
			System.out.println("Download canceled: " + this.getFileName());
		}
	}

	public static void main(String[] args) throws Exception {
		RedTube yt = new RedTube(new URL("http://www.redtube.com/8086"), null,
				null);
		yt.run();

	}
	/*
	 * @Override public void run() { try { URL url = new
	 * URL("http://www.videodl.org/"); InputStream is =
	 * url.openConnection().getInputStream(); String page =
	 * Parser.convertStreamToString(is, false);
	 * 
	 * String requestForm = Parser.getComplexTag("form", page).get(0); String
	 * action = "http://www.videodl.org" + Parser.getAttribute("action",
	 * requestForm); System.out.println(action); Request request =
	 * Hoster.readRequestFormular(requestForm); request.setAction(action);
	 * request.addParameter("searchinput", this.getUrl().toString()); is =
	 * request.request(); page = Parser.convertStreamToString(is, true); String
	 * link = Parser.getSimpleTag("a", page).get(0).replace("\\\"", "\"");
	 * this.setDirectUrl(new URL(Parser.getAttribute("href", link) .replace(" ",
	 * "&nbsp;"))); this.isStopped(); this.isCanceled();
	 * ServerDownload.download(this); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

}
