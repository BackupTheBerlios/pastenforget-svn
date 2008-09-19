package core.hoster;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import core.Download;

public class FileFactory extends Download {
	
	
	@Override
	protected URLConnection getRequestPage(URL url) throws IOException {
		InputStream in = url.openConnection().getInputStream();
		Tidy tidy = new Tidy();
		tidy.setShowWarnings(false);
		Document node = tidy.parseDOM(in, null);
		String link = getLink(node, "http://filefactory.com", "dlf");
		
		url = new URL(link);
		in = url.openConnection().getInputStream();
		node = tidy.parseDOM( in, null);
		
		Node iframe = node.getElementsByTagName("iframe").item(0);
		Node src = iframe.getAttributes().getNamedItem("src");
		
		URLConnection postRequestPage = new URL("http://filefactory.com" + src.getNodeValue()).openConnection();
		
		return postRequestPage;
	}
	
	private String identifyFilename(String urlPath) {
		Pattern p = Pattern.compile("[^/]+");
		Matcher m = p.matcher(urlPath.replaceAll("_", "."));
		String fileName = new String();
		while(m.find()) {
			fileName = m.group();
		}
		
		return fileName;
	}
	
	@Override
	public boolean startDownload() throws MalformedURLException, IOException {
		URL url = new URL("http://www.filefactory.com/file/debdd1/n/pb403_part1_rar"); //this.getUrl();
		String fileName = identifyFilename(url.getPath());
		System.out.println(fileName);
		this.setFilename(fileName);
		
		InputStream postRequestPage = getRequestPage(url).getInputStream();
		String encodedParameters = requestPrepare(postRequestPage, "http://filefactory.com", "secur");
		URLConnection postMethodResponse = request("http://filefactory.com/check/", encodedParameters);
		InputStream responseStream = postMethodResponse.getInputStream();

		Tidy tidy = new Tidy();
		tidy.setShowWarnings(false);
		Document downloadPageDOM = tidy.parseDOM(responseStream, null);
		if(downloadPageDOM.getElementsByTagName("form").getLength() > 0) {
			this.startDownload();
		} else {
			NodeList aNodes = downloadPageDOM.getElementsByTagName("a");
			Iterator<Node> aNodeIterator = new NodeListIterator(aNodes);
			while(aNodeIterator.hasNext()) {
				Node current = aNodeIterator.next();
				Node href = current.getAttributes().getNamedItem("href");
				if(href != null) {
					System.out.println(href.getNodeValue());
				}
				
			}
		}	
		
		return true;
	}
	
	
	public static void main(String[] args) throws IOException {
		Download fileFactory = new FileFactory();
		fileFactory.startDownload();
	}
	

	
	
}
