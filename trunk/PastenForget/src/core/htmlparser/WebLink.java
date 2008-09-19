package core.htmlparser;

import java.util.List;
import java.util.Map;

public class WebLink {
	private final List<Object> attributeNames;
	private final Map<String, String> attributeValues;
	
	
	public WebLink(List<Object> attributeNames, Map<String,String> attributeValues) {
		this.attributeNames = attributeNames;
		this.attributeValues = attributeValues;
	}
	
	public void addAttribute(String name, String value) {
		this.attributeNames.add(name);
		this.attributeValues.put(name, value);
	}
	
	public String getAttributeValue(String name) {
		return this.attributeValues.get(name);
	}
	
	public List<Object> getAttributeNames() {
		return this.attributeNames;
	}
	
	public void writeTag() {
		String writer = "<a";
		for(int i = 0; i < this.attributeNames.size(); i++) {
			writer += " " + this.attributeNames.get(i)+"=\""+this.getAttributeValue((String)this.attributeNames.get(i)) + "\"";
		}
		writer += "</a>";
		
		System.out.println(writer);
	}
	
	
	
	
}
