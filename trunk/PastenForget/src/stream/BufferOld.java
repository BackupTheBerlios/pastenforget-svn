package stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Synchronisierter Puffer fuer einen Download.
 * @author cpieloth
 *
 */
public class BufferOld {
	private byte[] buffer = new byte[65535];
	private final InputStream in;
	private int receivedBytes = 0;
	private boolean isAvailable = false;
	
	/*
	 * Falls benoetigt, wenn Download abgeschlossen.
	 * Ansonsten mit get und set loeschen.
	 */
	private boolean isComplete = false;

	public BufferOld(InputStream in) {
		this.in = in;
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
			receivedBytes = in.read(this.buffer);
		} catch(IOException e) {
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
