package IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class Web {
	private final int BUFFER_SIZE = 64;
	private final String url;
	
	public Web(String url) {
		this.url = url;
	}
	
	public String readText() throws IOException {
		HttpConnection connection = (HttpConnection)Connector.open(this.url);
		InputStream iStream = connection.openInputStream();
		InputStreamReader reader = new InputStreamReader(iStream);
		StringBuffer stringBuffer = new StringBuffer();
		char[] charBuffer = new char[BUFFER_SIZE];
		while (reader.read(charBuffer) > 0) {
			stringBuffer.append(charBuffer);
		}
		return stringBuffer.toString();
	}
	
}
