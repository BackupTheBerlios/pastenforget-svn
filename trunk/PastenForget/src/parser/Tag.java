package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.TagNotSupportedException;

public class Tag {
	private final String tag;
	private boolean complexTag = false;

	private class Position {
		private Integer start;
		private Integer end;

		public Position(int start) {
			this.start = start;
		}

		public Position(int start, int end) {
			this.start = start;
			this.end = end;
		}

		public void setStart(int start) {
			this.start = start;
		}

		public void setEnd(int end) {
			this.end = end;
		}

		public Integer getStart() {
			return this.start;
		}

		public Integer getEnd() {
			return this.end;
		}
	}

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
		int counter = 0;
		List<Position> foundPos = new ArrayList<Position>();

		String regex = "<[/]?" + tagName + "[^>]*>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(this.tag);
		while (m.find()) {
			if (m.group().indexOf("/") != 1) {
				// Start-Tag
				Position current = new Position(m.start());
				foundPos.add(counter, current);
				counter++;
			} else {
				// End-Tag
				counter--;
				Position current = foundPos.get(counter);
				current.setEnd(m.end());
				foundPos.set(counter, current);
			}
		}

		List<Tag> matches = new ArrayList<Tag>();
		for (Position pos : foundPos) {
			matches.add(new Tag(this.tag
					.substring(pos.getStart(), pos.getEnd())));
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
