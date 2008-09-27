package core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	
	public List<String> getComplexTag(String tagName, String page) {
		List<String> tags = new ArrayList<String>();
		String unique = String.valueOf((char)1);
		String replacedPage = page.replaceAll("</" + tagName + ">", unique);
		String regex = "<" + tagName + "[^" + unique + "]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(replacedPage);
		while(m.find()) {
			tags.add(m.group());
		}
		return tags;
	}
	
	public List<String> getSimpleTag(String tagName, String page) {
		List<String> tags = new ArrayList<String>();
		String regex = "<" + tagName + "[^>]*>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(page);
		
		while(m.find()) {
			tags.add(m.group());
		}
		
		return tags;
	}
	
	public String getAttribute(String attributeName, String node) {
		String regex = attributeName + "=\"[^\"]*\"";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(node);
		
		if(m.find()) {
			return m.group().replaceAll("\"", "").replaceAll(attributeName + "=", "");
		} else {
			return null;
		}
	}	
	
	
	
	
	
}
