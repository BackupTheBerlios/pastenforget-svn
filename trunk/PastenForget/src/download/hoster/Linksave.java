package download.hoster;

import java.util.ArrayList;
import java.util.List;

import parser.Tag;
import web.Connection;

public class Linksave {

	
	public static void main(String[] args) throws Exception {
		String link = "http://linksave.in/46957043049fa2af27fa31";
		Connection webConnection = new Connection();
		webConnection.connect(link);
		Tag document = webConnection.getDocument(false);
		List<Tag> tableCells = document.getComplexTag("td");
		List<String> decryptedLinks = new ArrayList<String>();
		for(Tag tableCell : tableCells) {
			if("11%".equals(tableCell.getAttribute("width"))) {
				List<Tag> targets = tableCell.getComplexTag("a");
				if(targets.size() > 0) {
					link = targets.get(0).getAttribute("href");
					webConnection.connect(link);
					document = webConnection.getDocument(false);
					link = document.getSimpleTag("frame").get(2).getAttribute("src");
					do {
						webConnection.connect(link);
						document = webConnection.getDocument(false);
					} while(document.getSimpleTag("iframe").size() == 0);
					String source = document.getSimpleTag("iframe").get(0).getAttribute("src");
					String[] parts = source.split(";");
					StringBuffer sb = new StringBuffer();
					for(String part : parts) {
						part = part.replaceAll("&#", "");
						sb.append(String.valueOf((char)(Integer.parseInt(part))));
					}
					decryptedLinks.add(sb.toString());
				}
			}
		}
		for(String decryptedLink : decryptedLinks) {
			System.out.println(decryptedLink);
		}
		
	}
	
}
