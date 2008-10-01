package stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Synchronisierter Puffer fuer einen Download.
 * 
 * @author cpieloth
 * 
 */
public class BufferDouble {
	private byte[] bufferA = new byte[65535];

	private byte[] bufferB = new byte[65535];

	private final InputStream in;

	private int receivedBytesA = 0;

	private int receivedBytesB = 0;

	private boolean isAvailableA = false;

	private boolean isAvailableB = false;
	
	private boolean firstA = true;

	private boolean firstB = true;

	private boolean read = true;

	private boolean write = true;

	private boolean writeA = true;

	private boolean writeB = false;

	private boolean readA = false;

	private boolean readB = false;

	/*
	 * Falls benoetigt, wenn Download abgeschlossen. Ansonsten mit get und set
	 * loeschen.
	 */
	private boolean isComplete = false;

	public BufferDouble(InputStream in) {
		this.in = in;
	}

	/**
	 * Liesst Daten aus dem Puffer, wenn neue Daten verfuegbar. Ansonsten warten
	 * bis Daten verfuegbar.
	 * 
	 * @return byte[]
	 */
	public Packet read() {
		// return readA();

		if (read) {
			read = !read;
			return readA();
		} else {
			read = !read;
			return readB();
		}

	}

	/**
	 * Schreibt neue DatenA in den Puffer, wenn er gelesen wurde. Ansonsten
	 * warten bis Daten gelesen wurden.
	 * 
	 * @param buffer
	 */
	public int write() {
		// return writeA();

		if (write) {
			write = !write;
			return writeA();
		} else {
			write = !write;
			return writeB();
		}

	}

	public boolean isComplete() {
		if (!isAvailableA && !isAvailableB && isComplete) {
			return true;
		} else {
			return false;
		}
	}

	public void setComplete() {
		this.isComplete = true;
	}

	public int writeA() {
		lockWriteA();
		System.out.println("WriteBufferA: locked");

		if (isAvailableA) {
			System.out.println("WriteBufferA: wait before");
			waitReadA();
			System.out.println("WriteBufferA: wait after");
		}

		try {
			receivedBytesA = in.read(bufferA);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (long i = 0; i < 1000000; i++)
			if (i % 10000 == 0)
				System.out.println("WriteA " + i);

		isAvailableA = true;
		unlockWriteA();
		return receivedBytesA;
	}

	public int writeB() {
		lockWriteB();
		System.out.println("WriteBufferB: locked");

		if (isAvailableB) {
			System.out.println("WriteBufferB: wait before");
			waitReadB();
			System.out.println("WriteBufferB: wait after");
		}

		try {
			receivedBytesB = in.read(bufferB);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (long i = 0; i < 1000000; i++)
			if (i % 10000 == 0)
				System.out.println("WriteB " + i);

		isAvailableB = true;
		unlockWriteB();
		return receivedBytesB;
	}

	public Packet readA() {
		System.out.println("ReadBufferA: start");
		lockReadA();
		if (!isAvailableA) {
			System.out.println("ReadBufferA: wait before");
			waitWriteA();
			System.out.println("ReadBufferA: wait after");
		}

		System.out.println("ReadBufferA: locked");
		for (long i = 0; i < 1000000; i++)
			if (i % 10000 == 0)
				System.out.println("ReadA " + i);
		isAvailableA = false;
		unlockReadA();
		return new Packet(bufferA, receivedBytesA);
	}

	public Packet readB() {
		System.out.println("ReadBufferB: start");
		
		lockReadB();
		if (!isAvailableB) {
			if (!isAvailableA) {
				System.out.println("ReadBufferB: wait before");
				waitWriteB();
				System.out.println("ReadBufferB: wait after");
			}
		}

		System.out.println("ReadBufferB: locked");
		for (long i = 0; i < 1000000; i++)
			if (i % 10000 == 0)
				System.out.println("ReadB " + i);
		isAvailableB = false;
		unlockReadB();
		return new Packet(bufferB, receivedBytesB);
	}

	private synchronized void lockReadA() {
		readA = true;
	}

	private synchronized void lockReadB() {
		readB = true;
	}

	private synchronized void unlockReadA() {
		readA = false;
	}

	private synchronized void unlockReadB() {
		readB = false;
	}

	private synchronized void lockWriteA() {
		writeA = true;
	}

	private synchronized void lockWriteB() {
		writeB = true;
	}

	private synchronized void unlockWriteA() {
		writeA = false;
	}

	private synchronized void unlockWriteB() {
		writeB = false;
	}

	private void waitReadA() {
		while (readA)
			;
	}

	private void waitReadB() {
		while (readB)
			;
	}

	private void waitWriteA() {
		while (writeA)
			;
	}

	private void waitWriteB() {
		while (writeB)
			;
	}

}
