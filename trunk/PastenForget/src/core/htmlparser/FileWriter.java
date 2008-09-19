package core.htmlparser;

import java.io.FileOutputStream;

public class FileWriter extends Thread {
	private final FileOutputStream fos;
	private final byte[] buffer;
	private final int length;
	
	public FileWriter(FileOutputStream fos, byte[] buffer, int length) {
		System.out.println("Beginn");
		this.fos = fos;
		this.buffer = buffer;
		this.length = length;
	}
	
	@Override
	public void run() {
		try {
			this.fos.write(this.buffer, 0, this.length);
			System.out.println("Ende");
		} catch(Exception e) {
			System.out.println("Kann nicht in File schreiben");
		}
	}
	
	
}
