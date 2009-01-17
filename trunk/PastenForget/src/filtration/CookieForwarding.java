package filtration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import parser.Tag;

public class CookieForwarding {
	private String cookie = new String();
	
	public Tag readData(InputStream is) {
		StringBuffer sb = new StringBuffer();
		try {
			String line = new String();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return new Tag(sb.toString());
	}
	
	public Tag get(String link) {
		Tag document = null;
		try {
			URL url = new URL(link);
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("Cookie", this.cookie);
			document = this.readData(connection.getInputStream());
			Map<String, List<String>> header = connection.getHeaderFields();
			List<String> cookies = header.get("Set-Cookie");
			String cookie = new String();
			if(cookies != null && (cookies.size() > 0)) {
				cookie = cookies.get(cookies.size() - 1);
				this.cookie = cookie.substring(0, cookie.indexOf(";"));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	public String getCookie() {
		return this.cookie;
	}

}
