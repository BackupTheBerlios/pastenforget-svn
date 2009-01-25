package parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Formular {
	private final Map<String, String> postParameters = new HashMap<String, String>();
	private final String action;
	private final String method;
	
	public Formular(Tag formular) {
		this.action = formular.getAttribute("action");
		this.method = formular.getAttribute("method");
		List<Tag> inputFields = formular.getSimpleTag("input");
		for(Tag inputField : inputFields) {
			String name = inputField.getAttribute("name");
			String value = inputField.getAttribute("value");
			if(name != null && !name.equals("")) {
				value = (value != null) ? value : new String();
				this.postParameters.put(name, value);
			}
		}		
	}
	
	public Map<String, String> getPostParameters() {
		return postParameters;
	}
	
	public String getAction() {
		return action;
	}
	
	public String getMethod() {
		return method;
	}
}
