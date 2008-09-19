package core.htmlparser;

import java.util.List;
import java.util.Map;

public class WebForm {
	private final List<Object> attributeNames;
	private final Map<String, String> attributeValues;
	private final String action;
	
	public WebForm(String action, List<Object> attributeNames, Map<String,String> attributeValues) {
		this.attributeNames = attributeNames;
		this.attributeValues = attributeValues;
		this.action = action;
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
	
	public void setCaptcha(String captcha) {
		for(int i = 0; i < attributeNames.size(); i++) {
			if(attributeValues.get(attributeNames.get(i)) == null) {
				attributeValues.put((String)attributeNames.get(i), captcha);
			}
		}
	}
	
	public String getAction() {
		return this.action;
	}
	
	public void submit() {
		
	}
	
	
	public void writeTag() {
		String writer = "<form action=\"" + this.action + "\">";
		for(int i = 0; i < this.attributeNames.size(); i++) {
			writer += "\n\t<input " + "name=\"" + this.attributeNames.get(i) + "\" " + "value=\"" + this.getAttributeValue((String)this.attributeNames.get(i)) + "\">";
		}
		writer += "\n</form>";
		
		System.out.println(writer);
	}
	
	
	
	
}
