package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Die Klasse dient als Parser für einen gegebenen HTML String.
 *  Dabei wird kein kompletter DOM-Baum aufgebaut, d.h.
 *  nur die Teile geparst, die benötigt werden.
 * 
 * @author christopher
 *
 */
public class Tag {
	private final String tag;
	private boolean complexTag = false;

	
	public Tag(String tag) {
		this.tag = tag;
		this.complexTag = this.checkIfComplexTag();

	}

	/**
	 * Die Method bestimmt für einen gegebenen Attributname den Wert.
	 * @param attributeName
	 * @return attributeValue
	 */
	public String getAttribute(String attributeName) {
		String input = this.tag;
		if (this.complexTag) {
			String regex = "<[^>]*>";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(this.tag);
			m.find();
			input = m.group();
		}
		String regex = attributeName + "=\"[^\"]*\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		if (m.find()) {
			return m.group().replaceAll("\"", "").replaceAll(
					attributeName + "=", "");
		} else {
			return null;
		}
	}

	/**
	 * Überprüft, ob der übergebene String einen Start und EndTag enthält.
	 * @return
	 */
	private boolean checkIfComplexTag() {
		String tag = this.tag;
		return (tag.matches("<[^>]*>.*<[^>]*>"));
	}

	@Override
	public String toString() {
		return this.tag;
	}

	/**
	 * Liefert eine Liste aller komplexen Tags die den übergebenen Tagname besitzen. 
	 * @param tagName
	 * @return
	 */
	public List<Tag> getComplexTag(String tagName) {
		Stack<Integer> startPositions = new Stack<Integer>();
		List<Tag> matches = new ArrayList<Tag>();
		String regex = "<[/]?" + tagName + "[^>]*>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(this.tag);
		while (m.find()) {
			if (m.group().indexOf("/") != 1) {
				startPositions.push(m.start());
			} else if (!startPositions.isEmpty()) {
				Integer startIndex = startPositions.pop();
				matches.add(new Tag(this.tag.substring(startIndex, m.end())));
			} else {
				// Fehlerkorrektur HTML Code (falls zu viele EndTags)
				continue;
			}
		}

		// Fehlerkorrektur HTML Code (falls zu viele StartTags)
		while (!startPositions.isEmpty()) {
			Integer startIndex = startPositions.pop();
			matches.add(new Tag(this.tag.substring(startIndex, this.tag
					.length())));
		}

		return matches;
	}

	/**
	 * Liefert eine Liste aller einfachen Tags die den übergebenen Tagname besitzen.
	 * @param tagName
	 * @return
	 */
	public List<Tag> getSimpleTag(String tagName) {
		String regex = "<" + tagName + "[^>]*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(this.tag);
		List<Tag> matches = new ArrayList<Tag>();
		while (m.find()) {
			matches.add(new Tag(m.group()));
		}

		return matches;
	}

	/**
	 * Liefert eine Liste aller Javascript Variablen-Deklarationen
	 * @return
	 */
	public List<Tag> getJavascript() {
		String regex = "var[^;]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(this.tag);
		List<Tag> matches = new ArrayList<Tag>();
		while (m.find()) {
			matches.add(new Tag(m.group()));
		}

		return matches;
	}
	

	public Tag getElementById(String tagName, String id) {
		List<Tag> elements = this.getComplexTag(tagName);
		for(Tag element : elements) {
			if(id.equals(element.getAttribute("id"))) {
				return element;
			}
		}
		elements = this.getSimpleTag(tagName);
		for(Tag element : elements) {
			if(id.equals(element.getAttribute("id"))) {
				return element;
			}
		}
		return null;
	}
	
	public List<Tag> getElementsByClass(String tagName, String cssClass) {
		List<Tag> foundMatches = new ArrayList<Tag>();
		List<Tag> elements = this.getComplexTag(tagName);
		for(Tag element : elements) {
			if(cssClass.equals(element.getAttribute("class"))) {
				foundMatches.add(element);
			}
		}
		elements = this.getSimpleTag(tagName);
		for(Tag element : elements) {
			if(cssClass.equals(element.getAttribute("class"))) {
				foundMatches.add(element);
			}
		}
		return foundMatches;
	}
}
