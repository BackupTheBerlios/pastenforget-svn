package settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import middleware.Tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Languages {
	private static final List<String> languages = new ArrayList<String>();
	private static Map<String, String> vocabularies = new HashMap<String, String>();
	private static final File languageFile = new File(Tools.getProgramPath()
			.getAbsolutePath()
			+ "/pnf-languages.xml");
	private static String language = "Deutsch";

	public Languages() {
		restore();
	}
	
	public static ArrayList<String> getLanguages() {
		// TODO Sprachen auslesen, dann zurück geben
		return null;
	}

	public static String getTranslation(String key) {
		if (vocabularies.containsKey(key)) {
			return vocabularies.get(key);
		} else {
			return "NO TEXT";
		}
	}
	
	public static void setLanguage(String lang) {
		language = lang;
	}

	public static void restore() {
		vocabularies = new HashMap<String, String>();
		if (languageFile.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document dom = null;
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				dom = db.parse(languageFile);

				Element rootElement = dom.getDocumentElement();
				Nodes languageNodes = new Nodes(rootElement
						.getElementsByTagName("language"));
				for (Node languageNode : languageNodes) {
					String languageName = languageNode.getAttributes()
							.getNamedItem("name").getNodeValue();
					languages.add(languageName);
					//TODO Sprachname auslesen und "English" ersetzen
					if (languageName.equals(language)) {
						Nodes keyNodes = new Nodes(((Element) languageNode)
								.getElementsByTagName("key"));
						for (Node keyNode : keyNodes) {
							String key = keyNode.getAttributes().getNamedItem(
									"name").getNodeValue();
							String value = keyNode.getChildNodes().item(0)
									.getNodeValue();
							vocabularies.put(key, value);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Languages();
	}

}
