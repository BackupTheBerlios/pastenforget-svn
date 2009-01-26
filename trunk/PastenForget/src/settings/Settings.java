package settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import middleware.Tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * In dieser Klasse werden alle Einstellungen verwaltet. Alle Einstellung
 * koennen abgerufen oder gespeichert werden.
 * 
 * @author executor
 * 
 */

public class Settings {
	private static File downloadDirectory = new File(Tools.getProgramPath()
			.getAbsolutePath());

	private static File srcDirectory = new File(Tools.getProgramPath()
			.getAbsolutePath());

	private static int userInterface = 0;

	private static String language = "English";

	private static final File settingsFile = new File(Tools.getProgramPath()
			.getAbsolutePath()
			+ "/pnf-settings.xml");

	private static Document dom;

	/*
	public Settings() {
		this.restore();
	}
	*/

	public static void setDownloadDirectory(File downloadDir) {
		if (!"".equals(downloadDir.getPath())
				&& (downloadDir != null)) {
			downloadDirectory = downloadDir;
		}
	}

	public static File getDownloadDirectory() {
		return downloadDirectory;
	}

	public static void setSrcDirectory(File srcDir) {
		if (!"".equals(srcDir.getPath()) && (srcDir != null)) {
			srcDirectory = srcDir;
		}
	}

	public static File getSrcDirectory() {
		return srcDirectory;
	}

	public static void setUserInterface(int userInt) {
		for (LookAndFeelEnum enu : LookAndFeelEnum.values()) {
			if (enu.getKey() == userInt) {
				userInterface = userInt;
				return;
			}
		}
		userInterface = LookAndFeelEnum.STANDARD.getKey();
	}

	public static String getLanguage() {
		return language;
	}

	public static void setLanguage(String lang) {
		for (String langOri : Languages.getLanguages()) {
			if (langOri.equals(language)) {
				language = lang;
				return;
			}
		}
		language = "English";
	}

	public static int getUserInterface() {
		return userInterface;
	}

	public static void save() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element rootElement = dom
				.createElement(SettingsEnum.SETTINGS.getName());
		dom.appendChild(rootElement);

		Element nextElement = dom.createElement(SettingsEnum.LOOKANDFEEL
				.getName());
		nextElement.setTextContent(Integer.toString(getUserInterface()));
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement(SettingsEnum.DOWNLOADDIR.getName());
		nextElement
				.setTextContent(getDownloadDirectory() != null ? getDownloadDirectory()
						.getPath()
						: "");
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement(SettingsEnum.SRCDIR.getName());
		nextElement
				.setTextContent(getSrcDirectory() != null ? getSrcDirectory()
						.getPath() : "");
		rootElement.appendChild(nextElement);

		nextElement = dom.createElement(SettingsEnum.LANGUAGE.getName());
		nextElement.setTextContent(getLanguage());
		rootElement.appendChild(nextElement);

		try {
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(
					settingsFile), format);

			serializer.serialize(dom);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean restore() {
		if (settingsFile.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				dom = db.parse(settingsFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Element rootElement = dom.getDocumentElement();
			NodeList actNodes;
			actNodes = rootElement
					.getElementsByTagName(SettingsEnum.LOOKANDFEEL.getName());
			if (actNodes.getLength() >= 1) {
				setUserInterface(Integer.parseInt(actNodes.item(0)
						.getTextContent()));
			}

			actNodes = rootElement
					.getElementsByTagName(SettingsEnum.DOWNLOADDIR.getName());
			if (actNodes.getLength() >= 1) {
				setDownloadDirectory(new File(actNodes.item(0).getTextContent()));
			}

			actNodes = rootElement.getElementsByTagName(SettingsEnum.SRCDIR
					.getName());
			if (actNodes.getLength() >= 1) {
				setSrcDirectory(new File(actNodes.item(0).getTextContent()));
			}

			actNodes = rootElement.getElementsByTagName(SettingsEnum.LANGUAGE
					.getName());
			if (actNodes.getLength() >= 1) {
				setLanguage(actNodes.item(0).getTextContent());
			}
			System.out.println("Settings.restore: done");
			return true;
		} else {
			System.out.println("Settings.restore: no file");
			return false;
		}
	}

}
