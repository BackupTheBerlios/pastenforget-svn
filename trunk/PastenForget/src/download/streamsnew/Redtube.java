package download.streamsnew;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import web.Connection;

public class Redtube {

	public void prepareConnection() throws IOException {
		String link = "http://www.redtube.com/12442";
		String[] urlParts = link.split("/");
		String videoId = urlParts[urlParts.length - 1];
		String flv = "http://embed.redtube.com/player/?id=" + videoId + "&style=redtube";
		
		Connection webConnection = new Connection();
		webConnection.connect(flv);
		String page = webConnection.doGet().toString();
		System.out.println(page);
	}
}
