package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	/**
	 * Konstante, welches ein nichtdruckbares Zeichen repräsentiert.
	 */
	private static final String EOT = String.valueOf((char) 1);

	/**
	 * Filtert aus einem HTML-String alle Elemente welche aus einem öffnenden
	 * und schließenden bestehen.
	 * 
	 * @param tagName
	 * @param htmlDocument
	 * @return complexTags
	 */
	public static List<String> getComplexTag(String tagName, String htmlDocument) {
		List<String> complexTags = new ArrayList<String>();

		String replacedPage = htmlDocument
				.replaceAll("</" + tagName + ">", EOT);
		String regex = "<" + tagName + "[^" + EOT + "]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(replacedPage);
		while (m.find()) {
			complexTags.add(m.group());
		}
		return complexTags;
	}

	/**
	 * Filtert den Inhalt aus einem HTML/XML-Element, mit öffnendem und
	 * schließenden Tag.
	 * 
	 * @param tagName
	 * @param htmlDocument
	 * @return content
	 */
	public static String getTagContent(String tagName, String htmlDocument) {
		String content = htmlDocument.replaceAll("<[/]?" + tagName + "[^>]*>",
				"");

		return content;
	}

	/**
	 * Filtert aus einem HTML-String alle Elemente welche nur einen Tag
	 * besitzen.
	 * 
	 * @param tagName
	 * @param htmlDocument
	 * @return simpleTags
	 */
	public static List<String> getSimpleTag(String tagName, String htmlDocument) {
		List<String> simpleTags = new ArrayList<String>();
		String regex = "<" + tagName + "[^>]*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(htmlDocument);
		while (m.find()) {
			simpleTags.add(m.group());
		}
		return simpleTags;
	}

	/**
	 * Filtert aus einem HTML-String alle Tags, welche keine Attribute besitzen.
	 * 
	 * @param tagName
	 * @param htmlDocument
	 * @return attributeLessTags
	 */
	public static List<String> getAttributeLessTag(String tagName,
			String htmlDocument) {
		List<String> attributeLessTags = new ArrayList<String>();

		String replacedPage = htmlDocument
				.replaceAll("</" + tagName + ">", EOT);
		String regex = "<" + tagName + ">" + "[^" + EOT + "]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(replacedPage);
		while (m.find()) {
			attributeLessTags.add(m.group());
		}
		return attributeLessTags;
	}

	/**
	 * Filtert aus einem HTML-Tag den Wert zu einem gewünschten Attribut.
	 * 
	 * @param attributeName
	 * @param simpleTag
	 * @return attributeValue
	 */
	public static String getAttribute(String attributeName, String simpleTag) {
		String regex = attributeName + "=\"[^\"]*\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(simpleTag);
		if (m.find()) {
			return m.group().replaceAll("\"", "").replaceAll(
					attributeName + "=", "");
		} else {
			return null;
		}
	}

	/**
	 * Filtert aus einem html-String Javascript Befehle und Deklarationen.
	 * 
	 * @param variable
	 * @param htmlDocument
	 * @return javascriptElements
	 */
	public static List<String> getJavaScript(String variable,
			String htmlDocument) {
		List<String> javascriptElements = new ArrayList<String>();
		String regex = variable + "[^;]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(htmlDocument);
		while (m.find()) {
			javascriptElements.add(m.group());
		}

		return javascriptElements;

	}

	/**
	 * Konvertiert einen InputStream in einen String. Als Option, kann der
	 * entstandene String für stdout ausgegeben werden.
	 * 
	 * @param in
	 * @param report
	 * @return htmlPage
	 * @throws IOException
	 */
	public static String convertStreamToString(InputStream in, boolean report)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String htmlPage = new String();
		String line = new String();

		if (report) {
			while (br.ready()) {
				line = br.readLine();
				System.out.println(line);
				htmlPage += line;
			}
		} else {
			while (br.ready()) {
				line = br.readLine();
				htmlPage += line;
			}
		}

		return htmlPage;
	}

	/**
	 * Bringt einen Dateinamen in eine taugliche Form.
	 * 
	 * @param fileName
	 * @return fileName (geparst)
	 */
	public static String parseFileName(String fileName) {
		String newFileName = fileName.replaceAll("&[^;]+;", "").replaceAll("/",
				"-");

		return newFileName;
	}

	/**
	 * Liest alle Name/Value-Paare aus einem FORM-Element aus und fügt diese zu
	 * einem Request-Objekt hinzu.
	 * 
	 * @param requestForm
	 * @return request
	 */
	public static Request readRequestFormular(String requestForm) {
		Request request = new Request();
		List<String> input = Parser.getSimpleTag("input", requestForm);
		Iterator<String> inputIt = input.iterator();
		while (inputIt.hasNext()) {
			String currentInput = inputIt.next();
			String name = new String();
			String value = new String();
			if ((name = Parser.getAttribute("name", currentInput)) != null) {
				value = Parser.getAttribute("value", currentInput);
				request.addParameter(name, value);
			}
		}
		return request;
	}

}
