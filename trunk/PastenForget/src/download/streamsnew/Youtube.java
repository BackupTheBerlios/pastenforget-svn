package download.streamsnew;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import web.Connection;

public class Youtube {
	public void prepareConnection() throws IOException {
		Connection webConnection = new Connection();
		webConnection.connect("http://uk.youtube.com/watch?v=4pXfHLUlZf4&feature=related");
		String page = webConnection.doGet().toString();
		
		String regex = "var swfArgs[^;]+;";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(page.toString());
		m.find();
		String[] flashProperties = m.group().replaceAll(".*[{]{1}|[}]{1}.*|\\s+|\"+", "").split(",");
		Map<String, String> flashVars = new HashMap<String, String>();
		for(String ps : flashProperties) {
			String[] pair = ps.split(":");
			String key = pair[0];
			String value = pair[1];
			flashVars.put(key, value);
		}
		String flv = "http://youtube.com/get_video?video_id=" + flashVars.get("video_id") + "&t=" + flashVars.get("t");
		System.out.println(flv);
	}
}
