package web;

import java.net.HttpURLConnection;

import parser.Tag;

public class Test {
	
	public static void main(String[] args) throws Exception {
		/*Connection webConnection = new Connection();
		webConnection.connect("http://m.studivz.net");
		Tag document = webConnection.getDocument();
		Tag form = document.getComplexTag("form").get(0);
		Formular formular = new Formular(form);
		String action = formular.getAction();
		String method = formular.getMethod();
		Map<String, String> postParameters = formular.getPostParameters();
		
		
		
		postParameters.put("username", "christopher.schaedlich@gmx.de");
		postParameters.put("password", "cm7eqfWL");
		postParameters.remove("save");
		for(Map.Entry<String, String> entry : postParameters.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		webConnection.connect(action);
		webConnection.doPost(postParameters);
		
		document = webConnection.getDocument(true);*/
		
		Connection webConnection = new Connection();
		webConnection.connect("http://www.newsurl.de/r/feCFm52");
		Tag document = webConnection.getDocument(false);
		Tag containerTop = document.getElementsByClass("div", "container top").get(0);
		Tag a = containerTop.getSimpleTag("a").get(0);
		String link = a.getAttribute("href");
		webConnection.connect(link);
		document = webConnection.getDocument(false);
		
		
	}
	
}
