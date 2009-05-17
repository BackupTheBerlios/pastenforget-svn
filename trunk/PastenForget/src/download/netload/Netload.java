package download.netload;

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
import download.hoster.HosterUtilities;


public class Netload extends Download {
	public Netload(URL url, File destination) {
		super(url, destination);
	}

	@Override
	public void prepareDownload() throws ThreadDeath {
		HosterUtilities util = new HosterUtilities(this);
		try {
			Connection webConnection = new Connection();
			webConnection.connect(this.getUrl());
			Tag document = webConnection.getDocument(true);
			Tag a = document.getElementsByClass("a", "download_fast_link").get(0);
			String link = "http://netload.in/" + a.getAttribute("href").replaceAll("&amp;", "&");
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
			
			if("cancel".equals(captchaCode)) {
				this.stop();
			}
			
			System.out.println(captchaCode);
			Tag form = document.getComplexTag("form").get(0);
			Formular formular = new Formular(form);
			String action = "http://netload.in/" + formular.getAction();
			Map<String, String> postParameters = formular.getPostParameters();
			postParameters.put("captcha_check", captchaCode);
			webConnection.connect(action);
			webConnection.doPost(postParameters);
			document = webConnection.getDocument(true);
			String directLink = "";
			try {
				directLink = document.getElementsByClass("a", "Orange_Link").get(0).getAttribute("href");
				util.download(directLink);
			} catch(Exception e) {
				this.stop();
			}
			
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
}