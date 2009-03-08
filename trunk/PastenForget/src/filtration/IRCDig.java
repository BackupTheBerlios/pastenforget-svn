package filtration;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class IRCDig {

	public static void main(String[] args) throws Exception {
		URL url = new URL("http://www.ircdig.com/");
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		Map<String, List<String>> header = connection.getHeaderFields();
		for(Map.Entry<String, List<String>> entrySet :header.entrySet() ) {
			for(String value : entrySet.getValue()) {
				System.out.println(entrySet.getKey() + ": " + value);
			}
		}

		url = new URL("http://www.ircdig.com/bin/index.pl");
		connection = url.openConnection();
		
		String query = "nav=01&sub=01&phrase=xvid&sort=1&botfilter=1&submit=Search";
		connection.setUseCaches(true);
		connection.setDefaultUseCaches(true);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; de; rv:1.9.0.5) Gecko/2008121621 Ubuntu/8.04 (hardy) Firefox/3.0.5");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", "58");

		OutputStream oStream = connection.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(oStream);
		writer.write(query);
		writer.flush();
		writer.close();
		
		InputStream iStream = connection.getInputStream();
		OutputStream foStream = new FileOutputStream("/home/christopher/Desktop/test.txt");
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = iStream.read(buffer)) > 0) {
			foStream.write(buffer, 0, len);
		}
		foStream.flush();
		foStream.close();
		
		
	}
	
}