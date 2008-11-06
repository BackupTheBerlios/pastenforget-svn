package download.irc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class Reader extends Thread {
	private final BufferedReader br;
	private final BlockingQueue<String> eventQueue;
	private boolean closed = false;
	
	public Reader(InputStream is, BlockingQueue<String> eventQueue) {
		this.br = new BufferedReader(new InputStreamReader(is));;
		this.eventQueue = eventQueue;
	}	
	
	@Override
	public void run() {
		String line = new String();
		try {
			while (!this.isClosed() && ((line = this.br.readLine()) != null)) {
				this.eventQueue.put(line);
			}
			if(!this.isClosed()) {
				this.eventQueue.put("connection lost");
			}
			this.br.close();
		} catch (IOException e) {
			System.out.println("Reader error");
		} catch (InterruptedException ie) {
			System.out.println("Reader error");
		}
	}
	
	private synchronized boolean isClosed() {
		return this.closed;
	}
	
	public synchronized void close() {
		this.closed = true;
	}
}
