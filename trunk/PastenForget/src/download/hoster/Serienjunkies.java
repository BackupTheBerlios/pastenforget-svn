package download.hoster;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import parser.Formular;
import parser.Tag;
import web.Connection;
import download.Download;

public class Serienjunkies extends Download {

	public Serienjunkies(URL url, File destination) {
		super(url, destination);
	}
	
	@Override
	public void prepareDownload() throws ThreadDeath {
		try {
			Connection webConnection = new Connection();
			webConnection.connect(this.getUrl());
			Tag document = webConnection.getDocument(false);
			Tag form = document.getComplexTag("form").get(0);
			Tag img = form.getSimpleTag("img").get(0);
			String imageLink = "http://download.serienjunkies.org" + img.getAttribute("src");
			webConnection.connect(imageLink);
			System.out.println(imageLink);
			Image captchaImage = webConnection.getImage("");
			this.setCaptcha(captchaImage);
			String captchaCode = new String();
			do {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				captchaCode = this.getCaptchaCode();
			} while (captchaCode.equals(""));
			Formular formular = new Formular(form);
			Map<String, String> postParameters = formular.getPostParameters();
			postParameters.put("c", captchaCode);
			String action = "http://download.serienjunkies.org" + formular.getAction();
			webConnection.connect(action);
			webConnection.doPost(postParameters);
			document = webConnection.getDocument(false);
			List<Tag> forms = document.getComplexTag("form");
			for(Tag f : forms) {
				action = f.getAttribute("action");
				if(action.matches("http://download[.]serienjunkies[.]org.*")) {
					webConnection.connect(action);
					document = webConnection.getDocument(false);
					Tag frame = document.getSimpleTag("frame").get(1);
					String source = frame.getAttribute("src");
					webConnection.connect(source);
					document = webConnection.getDocument(false);
					String redirectedLink = webConnection.getURL().toString();
					System.out.println(redirectedLink);
				}
			}
			this.cancel();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		Serienjunkies s = new Serienjunkies(new URL("http://download.serienjunkies.org/f-ff486583e3a1b315/rc_S02E04COL.html"), null);
		s.prepareDownload();
	}

}
