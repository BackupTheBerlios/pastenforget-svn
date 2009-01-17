package filtration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import parser.Tag;

public class LinkcryptWs {
	
	public static String decodeAscii(String coded) {
		String ascii = coded.replaceAll("&#", "");
		String[] letters = ascii.split(";");
		StringBuffer decoded = new StringBuffer();
		for(int i = 0; i < letters.length; i++) {
			decoded.append((char)Integer.valueOf(letters[i]).intValue());
		}
		
		return decoded.toString();
	}
	
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://linkcrypt.ws/1.php?u=MArCdSofFAE4uVPAimdD%2F9f%2FMUDi%2FWmxHcI4wkYIy9U8rPyqypGMl0E9rJAnY034dJjtWr18%2Fa9gotF%2FpM3prQ%3D%3D");
		URLConnection urlc = url.openConnection();
		InputStream is = urlc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = new String();
		StringBuffer sb = new StringBuffer();
		while((line = br.readLine()) != null) {
			sb.append(line);
		}
		Tag document = new Tag(sb.toString().replaceAll("\\s*=\\s*\"", "=\""));
		List<Tag> iframes = document.getSimpleTag("iframe");
		List<String> sources = new ArrayList<String>();
		for(Tag iframe : iframes) {
			String source = iframe.getAttribute("src");
			if(source != null) {
				sources.add(source);
			}
		}
		
		List<String> cookies = urlc.getHeaderFields().get("Set-Cookie");
		String cookie = new String();
		if(cookies != null && (cookies.size() > 0)) {
			cookie = cookies.get(cookies.size() - 1);
			cookie = cookie.substring(0, cookie.indexOf(";"));
		}
		
		System.out.println(cookie);
		
		
		for(String source : sources) {
			if(source.indexOf("#") != -1) {
				System.out.println("ascii:" + decodeAscii(source));
				url = new URL(decodeAscii(source));
				urlc = url.openConnection();
				urlc.addRequestProperty("Cookie", cookie);
				is = urlc.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				while((line = br.readLine()) != null) {
					System.out.println(line);
				}
			} else {
				System.out.println("nonas:" + source);
			}
		}
		
		
		
	}
}
