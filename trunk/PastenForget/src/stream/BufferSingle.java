package stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Synchronisierter Puffer fuer ein oder mehrere Threads.
 * 
 * @author cpieloth
 * 
 */
public class BufferSingle {
	private byte[] buffer = new byte[512]; //65535

	private final InputStream is;

	private int receivedBytes = 0;

	private boolean isAvailable = false;
	
	private boolean isComplete = false;

	public BufferSingle(InputStream in) {
		this.is = in;
	}

	/**
	 * Liesst Daten aus dem Puffer, wenn neue Daten verfuegbar. Ansonsten warten
	 * bis Daten verfuegbar.
	 * 
	 * @return byte[]
	 */
	public synchronized Packet read() {
		if (!isAvailable) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isAvailable = false;
		notify();
		try {
			Thread.sleep(6);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Packet(buffer, receivedBytes);
	}

	/**
	 * Schreibt neue Daten in den Puffer, wenn er gelesen wurde. Ansonsten
	 * warten bis Daten gelesen wurden.
	 * 
	 * @param buffer
	 */
	public synchronized int write() {
		if (isAvailable) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			receivedBytes = is.read(this.buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		isAvailable = true;
		notify();
		return receivedBytes;
	}

	public boolean isComplete() {
		return isComplete;
	}
	
	public void setComplete() {
		this.isComplete = true;
	}

}
