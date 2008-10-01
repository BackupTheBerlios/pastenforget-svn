package filtration;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import parser.Parser;

public class MirrorSearch {
	public static void main(String[] args) throws Exception {
		String[][] genres = {	{"apps", "movies", "games", "series", "mp3s", "xxx", "hoers", "cracks"},
								{"Programme", "Filme", "Spiele", "Serien", "Musik", "Porn", "eBooks", "Cracks"}
		};
		
		
		System.out.println("gefundene Einträge");
		for(int i = 0; i < genres.length; i++) {
			search("Linux", genres[0][i], genres[1][i]);
		}
		
		
	}
	
	public static void search(String search, String genre, String caption) throws Exception {
		URL url = new URL("http://ddl-warez.org/search.php?q=" + URLEncoder.encode(search, "UTF-8") + "&cat=" + genre);
		InputStream in = url.openConnection().getInputStream();
		String page = new String();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		while(br.ready()) {
			page += br.readLine();
		}
		
		page = Parser.getComplexTag("div id=\"main_content\"", page).get(0);
		
		List<String> links = Parser.getComplexTag("a", page);
		Iterator<String> it = links.iterator();
		while(it.hasNext()) {
			String current = it.next();
			String content = Parser.getTagContent("a", current).replaceAll("<img[^>]+>", "").replaceAll("<i.*/i>", "");
			content = content.replaceAll("&nbsp;", "").replaceAll("[^\\s\\w._-]*", "");
			System.out.println(content);
			OutputStream os = new FileOutputStream("/home/christopher/share/ddl-warez/" + content + ".txt");
			DDLWarez.main("http://ddl-warez.org/" + Parser.getAttribute("href", current), os);
			
			
		}
	}
}
