package stream;

public class Packet {
	private final byte[] buffer;
	private final int receivedBytes;
	
	public Packet(byte[] buffer, int receivedBytes) {
		this.buffer = buffer;
		this.receivedBytes = receivedBytes;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public int getReceivedBytes() {
		return receivedBytes;
	}

}
