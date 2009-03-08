package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.TagNotSupportedException;

public class Tag {
	private final String tag;
	private boolean complexTag = false;

	public Tag(String tag) {
		this.tag = tag;
		complexTag = this.checkIfComplexTag();

	}

	public String getTagContent(boolean removeInnerTags)
			throws TagNotSupportedException {
		if (this.complexTag) {
			String regex = "<[/]?[^>]*>";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(this.tag);
			m.find();
			String firstTag = m.group();
			regex = "\\w+";
			p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			m = p.matcher(firstTag);
			m.find();
			String tagName = m.group();
			regex = "<[/]?" + tagName + "[^>]*>";
			p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			m = p.matcher(this.tag);
			m.find();
			int start = m.end();
			int end = 0;
			while (m.find()) {
				end = m.start();
			}

			if (removeInnerTags) {
				return this.tag.substring(start, end).replaceAll("<[/]?[^>]*>",
						"");
			} else {
				return this.tag.substring(start, end);
			}
		} else {
			throw new TagNotSupportedException();
		}
	}

	public String getAttribute(String attributeName) {
		String input = new String();
		if (!this.complexTag) {
			input = this.tag;
		} else {
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

	private boolean checkIfComplexTag() {
		String tag = this.tag;

		if (tag.matches("<[^>]*>.*<[^>]*>")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.tag;
	}

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
