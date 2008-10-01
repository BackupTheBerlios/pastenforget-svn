package stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Synchronisierter Puffer fuer einen Download.
 * @author cpieloth
 *
 */
public class Buffer {
	private byte[] buffer1 = new byte[65535];
	private byte[] buffer2 = new byte[65535];
	private final InputStream in;
	private int receivedBytes = 0;
	private boolean isAvailable1 = false;
	private boolean isAvailable2 = false;
	private boolean buffer = false;
	/*
	 * Falls benoetigt, wenn Download abgeschlossen.
	 * Ansonsten mit get und set loeschen.
	 */
	private boolean isComplete = false;

	public Buffer(InputStream in) {
		this.in = in;
	}

	/**
	 * Liesst Daten aus dem Puffer, wenn neue Daten verfuegbar. Ansonsten warten
	 * bis Daten verfuegbar.
	 * 
	 * @return byte[]
	 */
	public synchronized Packet read() {
		if(buffer) {
			if (!isAvailable1) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			isAvailable1 = false;
			notify();
			System.out.println(buffer);
			buffer = !buffer;
			return new Packet(buffer1, receivedBytes);
		} else {
			if (!isAvailable2) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			isAvailable2 = false;
			notify();
			System.out.println(buffer);
			buffer = !buffer;
			return new Packet(buffer2, receivedBytes);
		}
	}

	/**
	 * Schreibt neue Daten in den Puffer, wenn er gelesen wurde. Ansonsten
	 * warten bis Daten gelesen wurden.
	 * 
	 * @param buffer
	 */
	public synchronized int write() {
		if(buffer) {
			if (isAvailable1) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				receivedBytes = in.read(this.buffer1);
			} catch(IOException e) {
				e.printStackTrace();
			}
			isAvailable1 = true;
			notify();
			return receivedBytes;
		} else {
			if (isAvailable2) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				receivedBytes = in.read(this.buffer2);
			} catch(IOException e) {
				e.printStackTrace();
			}
			isAvailable2 = true;
			notify();
			return receivedBytes;
		}
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete() {
		this.isComplete = true;
	}

}
