package download.hoster;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.Formular;
import parser.Tag;
import web.Connection;


public class Netload { //extends Download implements DownloadInterface {

	public Netload() {
		super();
	}

	public static void main(String[] args) throws Exception {
		Connection webConnection = new Connection();
		webConnection.setCookieForwarding(true);
		webConnection.connect("http://netload.in/dateiODY3Nzk4NT.htm");
		Tag document = webConnection.doGet();
		Tag divBox = document.getElementsByClass("div", "Free_dl").get(0);
		Tag link = divBox.getSimpleTag("a").get(0);
		String linkFreeUsers = "http://netload.in/" + link.getAttribute("href").replaceAll("&amp;", "&");
		System.out.println(linkFreeUsers);
		webConnection.connect(linkFreeUsers);
		document = webConnection.doPost(new HashMap<String, String>());
		List<Tag> images = document.getSimpleTag("img");
		String imageLink = new String();
		for(Tag image : images) {
			String source = image.getAttribute("src");
			if(source != null && source.matches("share/includes/captcha.php.*")) {
				imageLink = source;
				break;
			}
		}
		Tag form = document.getComplexTag("form").get(0);
		Formular formular = new Formular(form);
		Map<String, String> postParameters = formular.getPostParameters();
		String action = "http://netload.in/" + formular.getAction();
		
		webConnection.connect("http://netload.in/" + imageLink);
		webConnection.getImage();
		
		System.out.println("Captcha Code:");
		String captchaCode = new BufferedReader(new InputStreamReader(System.in)).readLine();
		
		postParameters.put("captcha_check", captchaCode);
		
		webConnection.connect(action);
		webConnection.doPost(postParameters);
	}
}
