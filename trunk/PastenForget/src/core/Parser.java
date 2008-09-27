package core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private final String EOT = String.valueOf((char) 1);

	public List<String> getComplexTag(String tagName, String htmlDocument) {
		List<String> tags = new ArrayList<String>();

		String replacedPage = htmlDocument
				.replaceAll("</" + tagName + ">", EOT);
		String regex = "<" + tagName + "[^" + EOT + "]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(replacedPage);
		while (m.find()) {
			tags.add(m.group());
		}
		return tags;
	}

	public List<String> getSimpleTag(String tagName, String htmlDocument) {
		List<String> tags = new ArrayList<String>();
		String regex = "<" + tagName + "[^>]*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(htmlDocument);
		while (m.find()) {
			tags.add(m.group());
		}
		return tags;
	}

	public String getAttribute(String attributeName, String simpleTag) {
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

}
