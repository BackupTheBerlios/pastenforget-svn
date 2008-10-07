package stream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Synchronisierter Puffer fuer ein oder mehrere Threads.
 * 
 * @author cpieloth
 * 
 */
public class Buffer {
	private byte[] buffer = new byte[2048]; //65535

	private final BufferedInputStream is;
	
	private final BufferedOutputStream os;

	private int receivedBytes = 0;
	
	private boolean isComplete = false;

	public Buffer(BufferedInputStream in, BufferedOutputStream out) {
		this.is = in;
		this.os = out;
	}

	/**
	 * Liesst Daten aus dem Puffer, wenn neue Daten verfuegbar. Ansonsten warten
	 * bis Daten verfuegbar.
	 * 
	 * @return byte[]
	 */
	public synchronized void read() {
		try {
			os.write(this.buffer, 0, this.receivedBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Schreibt neue Daten in den Puffer, wenn er gelesen wurde. Ansonsten
	 * warten bis Daten gelesen wurden.
	 * 
	 * @param buffer
	 */
	public synchronized int write() {
		try {
			this.receivedBytes = is.read(buffer);
			if(this.receivedBytes <= 0) {
				this.setComplete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.receivedBytes;
	}

	public boolean isComplete() {
		return isComplete;
	}
	
	public void setComplete() {
		this.isComplete = true;
	}

}
