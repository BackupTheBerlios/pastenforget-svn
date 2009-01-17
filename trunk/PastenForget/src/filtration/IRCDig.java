package filtration;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import parser.Tag;

public class IRCDig {

	/**
	 * Parameter:
	 * 
	 * Suchbegriff: inputString nav = 01 sub = 01 phrase = xvid sort = -1
	 * botfilter = 2
	 */
	
	public void readHeader(Map<String, List<String>> header) {
		for(String key : header.keySet()) {
			for(String value : header.get(key)) {
				System.out.println(key + ": " + value);
			}
		}
	}
	

	public static String join(List<String> parts, String seperator){
		if(parts.size() == 0) {
			return new String();
		}
		StringBuffer sb = new StringBuffer();
		for(String part : parts) {
			sb.append(seperator + part);
		}
		return sb.toString().substring(1);
	}
	
	public static void main(String[] args) throws Exception {
		CookieForwarding forwarding = new CookieForwarding();
		Tag document = forwarding.get("http://netload.in/dateiMTU2Mzk5MD.htm");
		List<Tag> links = document.getComplexTag("a");
		String action = new String();
		for(Tag link : links) {
			String href = link.getAttribute("href");
			if(href != null && (href.indexOf("captcha") != -1)) {
				action = href.replaceAll("&amp;", "&");
				break;
			}
		}
		
		System.out.println(forwarding.getCookie());
		document = forwarding.get("http://netload.in/" + action);
		
		List<Tag> images = document.getComplexTag("img");
		String imageLink = new String();
		for(Tag image : images) {
			String src = image.getAttribute("src");
			if(src != null && (src.indexOf("share/includes") != -1)) {
				imageLink = src;
			}
		}
		
		URL url = new URL("http://netload.in/" + imageLink);
		InputStream is = url.openConnection().getInputStream();
		OutputStream os = new FileOutputStream("/home/christopher/Desktop/captcha.jpg");
		int length;
		byte[] buffer = new byte[1024];
		while((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		System.out.println("Bild geschrieben");
		System.out.println("Bitte Captcha Code eingeben!");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		
		
		
		Tag form = document.getComplexTag("form").get(0);
		List<Tag> inputs = form.getSimpleTag("input");
		List<String> parameters = new ArrayList<String>();
		for(Tag input : inputs) {
			String name = input.getAttribute("name");
			String value = (input.getAttribute("value") != null) ? input.getAttribute("value") : "";
			if(name != null) {
				if(name.equals("captcha_check")) {
					parameters.add(name + "=" + code);
				} else {
					parameters.add(name + "=" + URLEncoder.encode(value, "UTF-8"));
				}
			}
		}
		
		url = new URL("http://netload.in/index.php?id=10");
		URLConnection connection = url.openConnection();
		String encoded = join(parameters, "&");
		connection.setRequestProperty("Cookie", "__utma=152033190.18763428.1229801510.1229809201.1229811442.4; __utmz=152033190.1229801510.1.1.utmccn=(referral)|utmcsr=download.serienjunkies.org|utmcct=/go-66fcc3590a4800cb5650fff737ed5add0891dd03780aa6f7068fa9fab644c89133bbcd961a10f4ea70005468a9a1fdeab8f01663a6420a1bc81913490e551d4c/|utmcmd=referral; cookie_counter=1229804898; PHPSESSID=a545312122cbae3d1579d510ff14ecde; __utmb=152033190; __utmc=152033190");
		connection.setUseCaches(true);n 
		connection.setDefaultUseCaches(true);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Cookie", forwarding.getCookie());
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", String.valueOf(encoded.length()));

		os = connection.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(os);
		writer.write(encoded);
		writer.flush();
		writer.close();
		
		System.out.println(forwarding.readData(connection.getInputStream()));
	}
	
}