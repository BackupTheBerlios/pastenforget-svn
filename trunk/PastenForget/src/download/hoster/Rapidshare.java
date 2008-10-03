package download.hoster;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Parser;
import parser.Request;
import queue.Queue;
import stream.ServerDownload;
import download.Download;

/**
 * Extrahiert Directlinks und fuehrt den Download aus. Hoster: rapidshare.com
 * 
 * @author cschaedl
 */

public class Rapidshare extends Download {
	private int counter = 0;
	
	public Rapidshare(URL url, File destination, Queue queue) {
		this.setUrl(url);
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFileName(createFilename());
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
			URL url = this.getUrl();
			InputStream in = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(in);
		
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
			page = Parser.convertStreamToString(in);
			List<String> headings = Parser.getComplexTag("h1", page);
			for(String current : headings) {
				if(Parser.getTagContent("h1", current).equals("Error")) {
					System.out.println("Error");
					this.setStatus("Slot belegt (" + ++counter + ")");
					Thread.sleep(10000);
					this.run();
				}
			}
			this.setStatus("Slot verfügbar");
			
			
			
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
		//Rapidshare rs = new Rapidshare(new URL("http://rapidshare.com/files/147616972/heroes-302-xor.part1.rar"), null);
		
	//	rs.run();
	}
	
	

}