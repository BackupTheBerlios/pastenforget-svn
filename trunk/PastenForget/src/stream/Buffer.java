package stream;

/**
 * Synchronisierter Puffer fuer einen Download.
 * @author cpieloth
 *
 */
public class Buffer {
	private byte[] buffer;

	private boolean isAvailable = false;

	public Buffer(byte[] buffer) {
		this.buffer = buffer;
	}

	/**
	 * Liesst Daten aus dem Puffer, wenn neue Daten verfuegbar. Ansonsten warten
	 * bis Daten verfuegbar.
	 * 
	 * @return byte[]
	 */
	public synchronized byte[] read() {
		if (!isAvailable) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isAvailable = false;
		notify();
		return buffer;
	}

	/**
	 * Schreibt neue Daten in den Puffer, wenn er gelesen wurde. Ansonsten
	 * warten bis Daten gelesen wurden.
	 * 
	 * @param buffer
	 */
	public synchronized void write(byte[] buffer) {
		if (isAvailable) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.buffer = buffer;
		isAvailable = true;
		notify();
	}

}
