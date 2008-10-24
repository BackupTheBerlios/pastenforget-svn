package settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import middleware.Tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Vergleicht die Schluessel in der language.xml.
 * 
 * @author executor
 *
 */

public class checkLanguageXML {

	private static final File languageFile = new File(Tools.getProgramPath()
			.getAbsolutePath()
			+ "/pnf-languages.xml");

	private static List<String> languages;

	private static List<String> keysSource = new ArrayList<String>();

	private static List<String> keysTest = new ArrayList<String>();

	private static String language = "English";

	public static List<String> getLanguages() {
		languages = new ArrayList<String>();
		if (languageFile.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(languageFile);

				Element rootElement = dom.getDocumentElement();
				Nodes languageNodes = new Nodes(rootElement
						.getElementsByTagName("language"));
				for (Node languageNode : languageNodes) {
					String languageName = languageNode.getAttributes()
							.getNamedItem("name").getNodeValue();
					languages.add(languageName);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return languages;
	}

	public static void getKeys() {
		languages = getLanguages();
		if (languageFile.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(languageFile);

				Element rootElement = dom.getDocumentElement();
				Nodes languageNodes = new Nodes(rootElement
						.getElementsByTagName("language"));
				for (Node languageNode : languageNodes) {
					String languageName = languageNode.getAttributes()
							.getNamedItem("name").getNodeValue();
					if (languageName.equals(language)) {
						Nodes keyNodes = new Nodes(((Element) languageNode)
								.getElementsByTagName("key"));
						for (Node keyNode : keyNodes) {
							String key = keyNode.getAttributes().getNamedItem(
									"name").getNodeValue();
							keysSource.add(key);
						}
						return;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void check() {
		getKeys();
		if (languageFile.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(languageFile);

				Element rootElement = dom.getDocumentElement();
				Nodes languageNodes = new Nodes(rootElement
						.getElementsByTagName("language"));
				System.out.println("Sourcelanguage: " + language);
				for (Node languageNode : languageNodes) {
					keysTest = new ArrayList<String>();
					String languageName = languageNode.getAttributes()
							.getNamedItem("name").getNodeValue();
					System.out.println("Languages start: " + languageName);

					Nodes keyNodes = new Nodes(((Element) languageNode)
							.getElementsByTagName("key"));

					for (Node keyNode : keyNodes) {
						String key = keyNode.getAttributes().getNamedItem(
								"name").getNodeValue();
						keysTest.add(key);
						if (!keysSource.contains(key)) {
							System.out
									.println(" - '"
											+ key
											+ "' nicht in Quellsprache enthalten, entfernen!");
						}
					}

					for (String keySrc : keysSource) {
						if (!keysTest.contains(keySrc)) {
							System.out
									.println(" - '"
											+ keySrc
											+ "' nicht in Zielsprache enthalten, hinzufügen!");
						}
					}

					System.out.println("Languages finish: " + languageName);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		check();
	}

}
