package core.htmlparser;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {
	
	public static void downloadFile(String host, String filename, String path)  throws MalformedURLException, IOException {
		URL url = new URL(host);
		InputStream is = url.openConnection().getInputStream();
		FileOutputStream fos = new FileOutputStream(path + filename);
		byte[] bufferbyte = new byte[65535];
		int length;
		while((length = is.read(bufferbyte)) > 0) {
			new FileWriter(fos, bufferbyte, length).start();
		}
	}
	
	public static String readCaptchacode() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String captcha = br.readLine();
		return captcha;
	}
	
	public static String downloadHTMLPage(String host) throws MalformedURLException, IOException {
		URL url = new URL(host);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String buffer;
		String page = new String();
		
		while((buffer = br.readLine()) != null) {
			page += buffer;
		}
		
		return page;
	}
	
}
