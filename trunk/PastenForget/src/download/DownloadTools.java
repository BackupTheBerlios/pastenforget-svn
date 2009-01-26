package download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import middleware.Middleware;
import middleware.Tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import queue.Queue;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import decrypt.RSDF;

import filtration.RequestPackage;

public class DownloadTools {
	
	private static Middleware middleware = null;

	public static final File downloadFile = new File(Tools.getProgramPath()
			.getAbsolutePath()
			+ "/pnf-downloadlist.xml");

	public static void setMiddleware(Middleware middleware) {
		DownloadTools.middleware = middleware;
	}
	
	/**
	 * Speichert die Downlownloads aller Warteschlangen in einer XML-Datei.
	 * @param file
	 * @return
	 */
	public static boolean saveDownloads(File file) {
		// XML vorbereiten
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
		} catch (Exception e) {
			System.out.println("DownloadTools.saveDownloads: failure");
		}

		Element rootElement = dom.createElement("downloadlist");
		dom.appendChild(rootElement);

		Element nextElement = null;
		Element childElement = null;

		// Warteschlangen durchlaufen
		Queue queue = null;
		for (HosterEnum hoster : HosterEnum.values()) {
			queue = middleware.getQueue(hoster.getName());
			if (!hoster.getName().equals(HosterEnum.IRC.getName())) {
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
			} else if (hoster.getName().equals(HosterEnum.IRC.getName())) {
				/*
				List<Download> downloads = queues.get(HosterEnum.IRC.getKey())
				.getDownloadList();
				String[][] ircDownloads = new String[downloads.size()][6];
				RequestPackage requestPackage;
				for (int i = 0; i < downloads.size(); i++) {
					requestPackage = downloads.get(i).getIrc();
					ircDownloads[i][0] = requestPackage.getIrcServer();
					ircDownloads[i][1] = requestPackage.getIrcChannel();
					ircDownloads[i][2] = requestPackage.getBotName();
					ircDownloads[i][3] = requestPackage.getPackage();
					ircDownloads[i][4] = requestPackage.getQueue();
				}
				*/
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
			System.out.println("DownloadTools.saveDownloads: failure");
		}

		return true;
	}

	/**
	 * Liest eine XML-Datei ein und fügt die Downloads zur Warteschlange hinzu.
	 * @param file
	 * @return
	 */
	public static boolean restoreDownloads(File file) {
		Document dom = null;
		if (downloadFile.exists()) {
			// XML vorbereiten
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				dom = db.parse(file);
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
					DownloadTools.addDownload(new URL(url), new File(destination));
				} catch (Exception e) {
					System.out.println("DownloadTools.restore: false URL or File");
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
					/*
					for (int i = 0; i < ircDownloads.length; i++) {
					requestPackage = new RequestPackage("", "",
							ircDownloads[i][4], "", "", ircDownloads[i][0],
							ircDownloads[i][1], ircDownloads[i][2],
							ircDownloads[i][3], "", "");
					downloadIrc(requestPackage);
				}
					 */
				}
				
			}
			return true;
		} else {
			System.out.println("DownloadTools.restore: failure");
			return false;
		}
	}
	
	/**
	 * Fügt einen Download zur Warteschlange hinzu.
	 * @param url
	 * @param destination
	 * @return
	 */
	public static boolean addDownload(URL url, File destination) {
		if (!(url.equals("") || url.equals("Kein Link angegeben!"))) {
			Download download = null;
			for (HosterEnum hoster : HosterEnum.values()) {
				if (url.toString().matches(hoster.getPattern())) {
					Class<?> myClass;
					try {
						myClass = Class.forName(hoster.getClassName());
						Constructor<?> constructor = myClass.getConstructor(URL.class, File.class);
						download = (Download) constructor.newInstance(url, destination);
						middleware.getQueue(hoster.getName()).addDownload(download);
						System.out.println("DownloadTools.addDownload: add " + url);
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("DownloadTools.addDownload: failure " + url);
						return false;
					}
				}
			}
			System.out.println("DownloadTools.addDownload: hoster not supported " + url);
			return false;
		} else {
			System.out.println("DownloadTools.addDownload: url failure");
			return false;
		}
	}
	
	/**
	 * Fügt einen IRC-Download zur Warteschlange hinzu.
	 * @param RequestPackage
	 */
	public static boolean addDownload(RequestPackage requestPackage, File destination) {
		return false;
		// TODO addDownload für IRC
		/*
		if (requestPackage != null) {
			Download download = new IRC();
			download.setIrc(requestPackage);
			download.setInformation(null, settings.Settings
					.getDownloadDirectory(), queues
					.get(HosterEnum.IRC.getKey()));
			queues.get(HosterEnum.IRC.getKey()).addDownload(download);
			System.out.println("Add download: " + requestPackage.getPackage());
			return true;
		} else {
			System.out.println("Add download: failure");
			return false;
		}*/
	}

	/**
	 * Liest eine Textdatei zeilenweise ein und fügt den Download zur Warteschlange hinzu.
	 * @param file
	 * @return
	 */
	public static boolean loadTextFile(File file) {
		if (file != null) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				System.out.println("DownloadTools.loadTextFile: file not found");
				e.printStackTrace();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			List<String> links = new ArrayList<String>();
			try {
				while (br.ready()) {
					links.add(br.readLine());
				}
			} catch (IOException e) {
				System.out.println("DownloadTools.loadTextFile: IOException");
				e.printStackTrace();
			}

			for (String url : links) {
				try {
					addDownload(new URL(url), settings.Settings.getDownloadDirectory());
				} catch (MalformedURLException e) {
					System.out.println("DownloadTools.loadTextFile: wrong URL format");
					e.printStackTrace();
				}
			}

			System.out.println("DownloadTools.loadTextFile: load " + file.getPath());
			return true;
		} else {
			System.out.println("DownloadTools.loadTextFile: no file");
			return false;
		}
	}
	
	/**
	 * Liest einen RSDF-Container ein.
	 * @param file
	 * @return
	 */
	public static boolean loadRsdf(File file) {
		if (file != null && file.exists()) {
			List<String> downloadList = RSDF.decodeRSDF(file);

			for (String url : downloadList) {
				try {
					addDownload(new URL(url), settings.Settings.getDownloadDirectory());
				} catch (MalformedURLException e) {
					System.out.println("DownloadTools.loadRsdf: wrong URL format");
					e.printStackTrace();
				}
			}

			System.out.println("DownloadTools.loadRsdf: load " + file.getPath());
			return true;
		} else {
			System.out.println("DownloadTools.loadRsdf: no file");
			return false;
		}
	}

}