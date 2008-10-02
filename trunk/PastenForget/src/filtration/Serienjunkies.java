package filtration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import parser.Parser;
import parser.Request;

public class Serienjunkies {
	
	
	public static void filterMirrors(URL url, File destination) throws Exception {
		InputStream is = url.openConnection().getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String page = new String();
		while(br.ready()) {
			page += br.readLine();
		}
		
		String form = Parser.getComplexTag("FORM", page).get(0);
		String action = "http://download.serienjunkies.org" + Parser.getAttribute("ACTION", form);
		Request request = new Request();
		request.setAction(action);;
		List<String> inputs = Parser.getSimpleTag("INPUT", form);
		for(String current : inputs) {
			String name = new String();
			if((name = Parser.getAttribute("NAME", current)) != null) {
				String value = Parser.getAttribute("VALUE", current);
				request.addParameter(name, value);
			}
		}
		String image = Parser.getSimpleTag("IMG", page).get(0);
		String captcha = "http://download.serienjunkies.org" + Parser.getAttribute("SRC", image);
		is = new URL(captcha).openConnection().getInputStream();
		OutputStream os = new FileOutputStream("serienjunkies_captcha.img");
		byte[] buffer = new byte[1024];
		int receivedBytes;
		while((receivedBytes = is.read(buffer)) != -1) {
			os.write(buffer, 0, receivedBytes);
		}
		
		System.out.println("Bitte geben Sie den Captcha Code ein!");
		br = new BufferedReader(new InputStreamReader(System.in));
		String captchaCode = br.readLine();
		request.addParameter("c", captchaCode);
		is = request.request();
		br = new BufferedReader(new InputStreamReader(is));
		page = new String();
		while(br.ready()) {
			page += br.readLine();
		}
		List<String> forms = Parser.getComplexTag("FORM", page);
		if(forms.size() < 2) {
			filterMirrors(url, destination);
		}
		
		
		List<String> directLinks = new ArrayList<String>();
		for(String current : forms) {
			if(current.indexOf("http://download.serienjunkies.org") != -1) {
				URL cryptedLink = new URL(Parser.getAttribute("ACTION", current));
				is = cryptedLink.openConnection().getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				page = new String();
				while(br.ready()) {
					page += br.readLine();
				}
				String frame = Parser.getSimpleTag("FRAME ", page).get(0);
				is = new URL(Parser.getAttribute("SRC", frame)).openConnection().getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				page = new String();
				while(br.ready()) {
					page += br.readLine();
				}
				String rsForm = Parser.getSimpleTag("form", page).get(0);
				directLinks.add(Parser.getAttribute("action", rsForm));
			}
		}
		
		for(String current : directLinks) {
			System.out.println(current);
		}	
	}
	
	
	
	public static void main(String[] args) throws Exception {
		filterMirrors(new URL("http://download.serienjunkies.org/f-8f5130bdd91b7711/rc_heroes303.html"), null);
	}
}
