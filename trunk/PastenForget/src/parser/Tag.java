package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

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

	public List<FormProperties> getFormulars() {
		List<FormProperties> requestForms = new ArrayList<FormProperties>();
		Map<String, String> properties;
		List<Tag> forms = this.getComplexTag("form");
		for (Tag form : forms) {
			properties = new HashMap<String, String>();
			String actionAttr = form.getAttribute("action");
			String action = (actionAttr == null) ? new String() : actionAttr;
			List<Tag> inputs = form.getSimpleTag("input");
			for (Tag input : inputs) {
				String name = input.getAttribute("name");
				String value = input.getAttribute("value");
				if (name != null) {
					if (value == null) {
						value = new String();
					}
					properties.put(name, value);
				}
			}
			FormProperties current = new FormProperties(properties, action);
			requestForms.add(current);
		}
		return requestForms;
	}
}
