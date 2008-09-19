package core.htmlparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HTMLParser {
	private String document;
	
	public HTMLParser(String document) {
		this.document = document;
	}
	
	
	private List<String> getTags(String regex) {
		List<String> tags = new ArrayList<String>();
		Pattern p;
		Matcher m;
		p = Pattern.compile( regex );
		m = p.matcher(this.document);
		while(m.find()) {
			tags.add(this.document.substring(m.start(), m.end()));
		}
		
		return tags;
	}
	
	private List<String> getTags(String regex, String doc) {
		List<String> tags = new ArrayList<String>();
		
		
		Pattern p;
		Matcher m;

		p = Pattern.compile( regex );
		m = p.matcher(doc);
		while(m.find()) {
			String[] splits = doc.substring(m.start(), m.end()).split("<form");
			for(int i = 0; i < splits.length; i++) {
				tags.add(splits[i]);
			}
		}
		
		return tags;
	}
	
	private Map<String,String> getAttributes(String tag) {
		Map<String,String> attributePairs = new HashMap<String,String>();
		Pattern p;
		Matcher m;
		String regex = "[^\\s]+=\"[^\"]+";
		
		p = Pattern.compile( regex );
		m = p.matcher(tag);
		while(m.find()) {
			String[] result = tag.substring(m.start(), m.end()).split("=\"");
			attributePairs.put(result[0], result[1]);
		}
		
		return attributePairs;
		
	}
	
	private WebImage createWebImage( Map<String,String> attributes ) {
		List<Object> attributeNames = Arrays.asList(attributes.keySet().toArray());
		
		return new WebImage(attributeNames, attributes);
	}
	
	private WebForm createWebForm(String action, Map<String,String> attributes ) {
		List<Object> attributeNames = Arrays.asList(attributes.keySet().toArray());
		
		return new WebForm(action, attributeNames, attributes);
	}
	
	private WebLink createWebLink(Map<String,String> attributes ) {
		List<Object> attributeNames = Arrays.asList(attributes.keySet().toArray());
		
		return new WebLink(attributeNames, attributes);
	}
	
	
	public List<WebImage> parseImage() {
		List<WebImage> img = new ArrayList<WebImage>();
		List<String> tags = this.getTags("<img [^>^;]+>");
		for(int i = 0; i < tags.size(); i++) {
			Map<String,String> attributes = this.getAttributes(tags.get(i));
			img.add(this.createWebImage(attributes));
		}
		
		return img;
	}
	
	
	public List<WebLink> parseLink() {
		List<WebLink> link = new ArrayList<WebLink>();
		List<String> tags = this.getTags("<a[^>]*>");
		for(int i = 0; i < tags.size(); i++) {
			System.out.println(tags.get(i));
			Map<String,String> attributes = this.getAttributes(tags.get(i));
			link.add(this.createWebLink(attributes));
		}
		
		return link;
	}
	
	
	public List<WebForm> parseForm() {
		List<WebForm> form = new ArrayList<WebForm>();
		System.out.println(this.document);
		this.document = this.document.replaceAll("</form>", "#");
		List<String> forms = this.getTags("<form[^>]*>[^#]*");
		forms.get(0);
		
		for(int i = 0; i < forms.size(); i++) {
			Map<String,String> requestparam = new HashMap<String,String>();
			String action = this.getAttributes(forms.get(i)).get("action");
			List<String> tags = this.getTags("<input[^>]*name[^>]*>", forms.get(i).replaceAll("<script.*/script>", ""));
			for(int j = 0; j < tags.size(); j++ ) {
				Map<String,String> attributes = this.getAttributes(tags.get(j));
				requestparam.put(attributes.get("name"), attributes.get("value"));
			}
			form.add(this.createWebForm(action, requestparam));
		}
		
		return form;
	}
	
	public List<String> parseJavaScriptVar() {
		// List<String> vars = this.getTags("var [\\w]? =[^;]*");
		List<String> vars = this.getTags("var [^;]*");
		return vars;
	}
	

	
	
}
