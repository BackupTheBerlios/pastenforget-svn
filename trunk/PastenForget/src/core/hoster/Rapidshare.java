package core.hoster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Download;
import core.Parser;
import core.Queue;
import core.Request;
import core.ServerDownload;

/**
 * Extrahiert Directlinks und fuehrt den Download aus. Hoster: rapidshare.com
 * 
 * @author cschaedl
 */

public class Rapidshare extends Download {
	
	public Rapidshare(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName("rapidshare");
	}
	
	private String convertStreamToString(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String page = new String();
		String line = new String();
	
		while((line = br.readLine()) != null) {
			page += line;
		}
		
		return page;
	}
	
	private String createFilename() {
		String file = this.getUrl().getFile();
		String regex = "[^/]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(file);
		String filename = new String();
		while (m.find()) {
			filename = m.group();
		}
		System.out.println(filename);

		return filename;
	}
	
	
	@Override
	public void run() {
		try {
			String filename = createFilename();
			this.setFileName(filename);
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			String page = convertStreamToString(in);
		
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
			page = convertStreamToString(in);

			List<String> inputs = Parser.getSimpleTag("input", page);
			inputIt = inputs.iterator();
			while(inputIt.hasNext()) {
				String current = inputIt.next();
				if(Parser.getAttribute("name", current) != null) {
					if(Parser.getAttribute("name", current).equals("mirror")) {
						String directLink = Parser.getAttribute("onclick", current).replaceAll("[^a-zA-Z0-9-.:/_]*", "");
						this.setDirectUrl(new URL(directLink.substring(directLink.indexOf("http"))));
						break;
					}
				}
			}
			
			int waitingTime = 0;
			List<String> vars = Parser.getJavaScript("var", page);
			Iterator<String> it = vars.iterator();
			while(it.hasNext()) {
				String current = it.next();
				if(current.matches(".*=[0-9\\s]+")) {
					current = current.replaceAll("[^0-9]+", "");
					waitingTime = new Integer(current);
					break;
				}
			}
			
			while(waitingTime > 0) {
				this.setStatus("Wartezeit: " + String.valueOf(waitingTime--) + " Sekunden");
				Thread.sleep(1000);
			}
			
			this.serverDownload = new ServerDownload(this);
			this.serverDownload.download();
			
		} catch(IOException e) {
			
		} catch(InterruptedException ie) {
			System.out.println("Download interrupted");
		} catch(Exception e) {
			
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		Rapidshare rs = new Rapidshare(new URL("http://rapidshare.com/files/147616972/heroes-302-xor.part1.rar"), null);
		
		rs.run();
	}
	
	

}