package middleware;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

	public static String getProgramPath() {
		String path = System.getProperty("java.class.path");
		String regex = "[^" + File.separator + "]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(path);
		int pos = 0;
		while (m.find()) {
			pos = m.start();
		}
		return path.substring(0, pos);
	}
}
