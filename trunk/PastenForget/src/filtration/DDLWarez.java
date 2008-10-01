package filtration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import parser.Parser;
import parser.Request;

public class DDLWarez extends Filtration {

	
	public DDLWarez( String url ) {
		super( url );
	}

	@Override
	public List<String> decrypt() {
		
		
		return null;
	}

	
	private String getDDLWarezPage( String input ) throws SAXException, IOException {
		
		
		
		return null;
	}
	
	
	public static void main(String link, OutputStream os) throws Exception {
		
		URL url = new URL(link);
		URLConnection urlc = url.openConnection();
		InputStream in = urlc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		String page = new String();
		while((line = br.readLine()) != null) {
			page += line;
		}
		
		String pwTable = page.substring(page.indexOf("Passwort:"));
		String password = Parser.getTagContent("td", Parser.getComplexTag("td", pwTable).get(0));
		os.write(("Passwort: " + password + "\n").getBytes());
		
		Iterator<String> it = Parser.getComplexTag("form", page).iterator();
		while(it.hasNext()) {
			String current = it.next();
			String action = "http://ddl-warez.org/" + Parser.getAttribute("action", current);
			String formName = Parser.getAttribute("name", current);
			
			if(formName.indexOf("dl") != 0) {
				continue;
			}
			Request request = new Request(action);
			Iterator<String> it2 = Parser.getSimpleTag("input", current).iterator();
			while(it2.hasNext()) {
				String current2 = it2.next();
				String name = Parser.getAttribute("name", current2);
				String value = Parser.getAttribute("value", current2);
				request.addParameter(name, value);	
			}
			in = request.request();
			String page2 = new String();
			br = new BufferedReader(new InputStreamReader(in));
			while((line = br.readLine()) != null) {
				page2 += line;
				
			}
			
			List<String> frames = Parser.getSimpleTag("FRAME", page2);
			for(int i = 0; i < frames.size(); i++) {
				if(frames.get(i).indexOf("http://rapidshare.com") > -1) {
					os.write((Parser.getAttribute("SRC", frames.get(i)) + "\n").getBytes());
				}
			}
			
		}
		
		
		
	}
	
	
	
}
