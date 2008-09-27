package core.hoster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import core.Download;
import core.Parser;
import core.Queue;
import core.Request;

/**
 * Extrahiert Directlinks und fuehrt den Download aus. Hoster: rapidshare.com
 * 
 * @author cschaedl
 */

public class Rapidshare extends Download {
	public final String uniqueImg = String.valueOf((char)1);
	public final String uniqueForm = String.valueOf((char)2);
	public final String uniqueA = String.valueOf((char)3);
	
	
	public Rapidshare(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFilename("rapidshare");
	}
	
	@Override
	public void downloadFileFromHoster() throws IOException {
		URL url = this.getUrl();
		InputStream in = url.openConnection().getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String page = new String();
		String line = new String();
		
		while((line = br.readLine()) != null) {
			page += line;
		}
		
		Request request = new Request();
		
		String requestForm = Parser.getComplexTag("form", page).get(0);
		String action = Parser.getAttribute("action", requestForm);
		
		request.setAction(action);
		
		List<String> input = Parser.getSimpleTag("input", requestForm);
		Iterator<String> inputIt = input.iterator();
		while(inputIt.hasNext()) {
			String currentInput = inputIt.next();
			String name = new String();
			String value = new String();
			if((name = Parser.getAttribute("name", currentInput)) != null) {
				value = Parser.getAttribute("value", currentInput);
				request.addParameter(name, value);
			}
		}
		
		in = request.request();
		
		br = new BufferedReader(new InputStreamReader(in));
		line = new String();
		
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
		
	}
	
	
	public static void main(String[] args) throws Exception {
		//Rapidshare uploaded = new Rapidshare(new URL("http://www.megaupload.com/de/?d=HH3TXA56"), null);
		Rapidshare rs = new Rapidshare(new URL("http://rapidshare.com/files/147616972/heroes-302-xor.part1.rar"), null);
		//Rapidshare mu = new Rapidshare(new URL("http://www.megaupload.com/de/?d=HH3TXA56"), null);
		
		
		rs.downloadFileFromHoster();
		
	}
	
	

}