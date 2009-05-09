package IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import GUI.HelloWorld;

public class File {
	private final int BUFFER_SIZE = 64;
	private final String fileName;
	
	public File(String fileName) {
		this.fileName = fileName;
	}
	
	public String readText() throws IOException {
		InputStream iStream = HelloWorld.class.getResourceAsStream(this.fileName);
		InputStreamReader reader = new InputStreamReader(iStream);
		StringBuffer stringBuffer = new StringBuffer();
		char[] charBuffer = new char[BUFFER_SIZE];
		while (reader.read(charBuffer) > 0) {
			stringBuffer.append(charBuffer);
		}
		reader.close();
		iStream.close();
		return stringBuffer.toString();
	}
}
