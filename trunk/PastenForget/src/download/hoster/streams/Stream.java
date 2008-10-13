package download.hoster.streams;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import parser.Parser;
import stream.ServerDownload;
import download.Download;
import exception.CancelException;
import exception.StopException;

public abstract class Stream extends Download {
	private String hosterCaption = new String(); 	
	
	public void setHosterCaption(String hosterCaption) {
		this.hosterCaption = hosterCaption;
	}
	
	public String getHosterCaption() {
		return this.hosterCaption;
	}
	
	public abstract String createFileName();
	
	@Override
	public void run() {
		try {
			this.setStatus("ermittle Dateiname");
			String filename = this.createFileName();
			this.setFileName(filename);
			this.setStatus("ermittle Directlink");
			String action = "http://www.redtubedownload.info/index.php?"
					+ "action=get_movie&ajax=true&url="
					+ URLEncoder.encode(this.getUrl().toString(), "UTF-8");
			URL url = new URL(action);
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, false);
			List<String> links = Parser.getSimpleTag("a", page);
			String targetLink = new String();
			for (String link : links) {
				if (link.indexOf(this.getHosterCaption()) != -1) {
					targetLink = link;
				}
			}
			if(Parser.getAttribute("href", targetLink) == null) {
				this.run();
			}
			this.setDirectUrl(new URL(Parser.getAttribute("href", targetLink)));
			if(this.isStopped()) {
				throw new StopException();
			}
			if(this.isCanceled()) {
				throw new CancelException();
			}
			ServerDownload.download(this);
		} catch(MalformedURLException e) {
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (StopException se) {
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException ce) {
			System.out.println("Download canceled: " + this.getFileName());
		}
	}	
	
	
	
	

}
