package download.hoster;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.Formular;
import parser.Tag;
import web.Connection;
import download.Download;
import download.DownloadThread;
import download.Status;


public class Netload extends Download {
	public Netload(URL url, File destination) {
		super(url, destination);
	}

	@Override
	public boolean cancel() {
		if (this.getThread() != null) {
			this.getThread().stop();
		}
		this.setStatus(Status.getCanceled());
		return true;
	}

	@Override
	public boolean start() {
		this.setStart(true);
		this.setThread(new DownloadThread((Runnable)this));
		this.getThread().start();
		this.setStatus(Status.getStarted());
		return false;
	}

	@Override
	public boolean stop() {
		if (this.getThread() != null) {
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
	public void prepareDownload() {
		//HosterUtilities util = new HosterUtilities(this);
		try {
			Connection webConnection = new Connection();
			webConnection.connect(this.getUrl());
			Tag document = webConnection.getDocument(false);
			Tag div = document.getElementsByClass("div", "Free_dl").get(0);
			String link = "http://netload.in/" + div.getSimpleTag("a").get(0).getAttribute("href").replaceAll("&amp;", "&");
			webConnection.connect(link);
			webConnection.doPost(new HashMap<String, String>());
			document = webConnection.getDocument(false);
			List<Tag> images = document.getSimpleTag("img");
			for(Tag image : images) {
				String source = image.getAttribute("src");
				if(source != null && source.matches("share/includes/captcha.php.*")) {
					String imageLink = "http://netload.in/" + source;
					webConnection.connect(imageLink);
					webConnection.doPost(new HashMap<String, String>());
					Image captchaImage = webConnection.getImage("/home/christopher/Desktop/" + "netLoadCaptcha.gif");
					this.setCaptcha(captchaImage);
					break;
				}
			}
			String captchaCode = "";
			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				captchaCode = this.getCaptchaCode();
			} while (captchaCode.equals(""));
			
			System.out.println(captchaCode);
			Tag form = document.getComplexTag("form").get(0);
			Formular formular = new Formular(form);
			String action = "http://netload.in/" + formular.getAction();
			Map<String, String> postParameters = formular.getPostParameters();
			postParameters.put("catpcha_check", captchaCode);
			webConnection.connect(action);
			webConnection.doPost(postParameters);
			document = webConnection.getDocument(true);
			
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
}