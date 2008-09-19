package core.htmlparser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Testklasse {
	public static void main(String[] args) throws Exception {
		//URL url = new URL("http://rapidshare.com/files/138763490/Sonic_Syndicate-Love_And_Other_Disasters-_Advance_-2008-mediaportal_by_Jho.rar");
		URL url = new URL("http://www.megaupload.com/de/?d=E0HU2K66");
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String buffer;
		String document = "";
		
		while((buffer = br.readLine()) != null) {
			document += buffer;
			//System.out.println(buffer);
		}
		// document = document.replaceAll("<script.*/script>", "");
		// System.out.println(document);
		
		HTMLParser parser = new HTMLParser(document);
		List<WebLink> links = parser.parseLink();
		for(int i = 0; i < links.size(); i++) {
			System.out.println(i + ": " + links.get(i).getAttributeValue("href"));
		}
		List<String> var = parser.parseJavaScriptVar();
		for(int i = 0; i < var.size(); i++) {
			System.out.println(var.get(i));
		}
		// parser.parseForm();
		/*
		List<WebImage> images = parser.parseImage();
		
		 for(int i = 0; i < images.size(); i++) {
			System.out.println("Image " + i);
			List<Object> attributes = images.get(i).getAttributeNames();
			for(int j = 0; j < attributes.size(); j++) {
				System.out.println(attributes.get(j) + " : " + images.get(i).getAttributeValue((String)attributes.get(j)) );
			}
		}
		*/
		
		
	}
}
