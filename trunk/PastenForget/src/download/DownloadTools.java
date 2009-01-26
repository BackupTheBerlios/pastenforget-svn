package download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import middleware.Middleware;
import middleware.Tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import queue.Queue;
import settings.SettingsEnum;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class DownloadTools {

	private static final File downloadFile = new File(Tools.getProgramPath()
			.getAbsolutePath()
			+ "/pnf-downloadlist.xml");

	public static boolean saveDownloads(Map<String, Queue> queues) {
		// XML vorbereiten
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
		} catch (Exception e) {
			System.out.println("DownloadTools: saveDownloads failure");
		}

		Element rootElement = dom.createElement("downloadlist");
		dom.appendChild(rootElement);

		Element nextElement = null;
		Element childElement = null;

		// Warteschlangen durchlaufen
		for (Queue queue : queues.values()) {
			if (queue != queues.get(HosterEnum.IRC.getName())) {
				for (Download download : queue.getDownloadList()) {
					nextElement = dom.createElement("download");

					childElement = dom.createElement("url");
					childElement.setTextContent(download.getUrl().toString());
					nextElement.appendChild(childElement);

					childElement = dom.createElement("destination");
					childElement.setTextContent(download.getDestination()
							.toString());
					nextElement.appendChild(childElement);

					rootElement.appendChild(nextElement);
				}
			} else if (queue == queues.get(HosterEnum.IRC.getName())) {
				// TODO save irc
			}
		}

		// XML in Datei speichern
		try {
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
					downloadFile), format);

			serializer.serialize(dom);
		} catch (IOException e) {
			System.out.println("DownloadTools: saveDownloads failure");
		}

		return true;
	}

	public static boolean restoreDownloads(Middleware middleware, Map<String, Queue> queues) {
		Document dom = null;
		if (downloadFile.exists()) {
			// XML vorbereiten
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				dom = db.parse(downloadFile);
			} catch (Exception e) {
				System.out.println("DownloadTools: restoreDownloads failure");
			}
			Element rootElement = dom.getDocumentElement();

			// Alle Downloads, außer IRC
			NodeList nodes = rootElement.getElementsByTagName("download");
			Node node = null;
			NodeList childs = null;
			String url = null;
			String destination = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				node = nodes.item(i);
				childs = node.getChildNodes();
				// FIXME XML wtf?				
				for (int j = 0; j < childs.getLength(); j=j*2+1) {
					if (childs.item(j).getNodeName() == "url") {
						url = childs.item(j).getTextContent();
					} else if (childs.item(j).getNodeName() == "destination") {
						destination = childs.item(j).getTextContent();
					} 
				}
				try {
					middleware.download(new URL(url));
				} catch (MalformedURLException e) {
					System.out.println("DownloadTools.restore: false URL");
				}
			}

			// Alle IRC-Downloads
			nodes = rootElement.getElementsByTagName("irc");
			node = null;
			childs = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				node = nodes.item(i);
				childs = node.getChildNodes();
				for (int j = 0; j < childs.getLength(); j=j*2+1) {
					// TODO irc restore
					if (childs.item(j).getNodeName() == "server") {
						// ...
					} else if (childs.item(j).getNodeName() == "channel") {
						// ...
					}  else if (childs.item(j).getNodeName() == "bot") {
						// ...
					} else if (childs.item(j).getNodeName() == "package") {
						// ...
					} else if (childs.item(j).getNodeName() == "filename") {
						// ...
					} else if (childs.item(j).getNodeName() == "destination") {
						// ...
					}
					// TODO zur Warteschlange hinzufügen
				}
				
			}
			return true;
		} else {
			System.out.println("DownloadTools.restore: failure");
			return false;
		}
	}

}