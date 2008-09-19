package core;

import java.io.FileOutputStream;
import java.io.IOException;

public class Logger {
	private FileOutputStream fos;
	private final String logfile;
	
	public Logger(String logfile) throws Exception {
		this.logfile = logfile;
		this.initLogger();
	}
	
	private void initLogger() {
		try {
			fos = new FileOutputStream(logfile);
		} catch(Exception e) { } 
		
	}
	
	public void log(Object message) throws IOException {
		fos.write((message.toString() + "\n").getBytes());
	}
}
