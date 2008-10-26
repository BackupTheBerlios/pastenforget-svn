package irc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;

public class Reader implements Runnable {
	private final InputStream sockIn;
	private final BlockingQueue<String> eventQueue;
	private boolean closed = false;
	
	public Reader(InputStream socketIn, BlockingQueue<String> eventQueue) {
		this.sockIn = socketIn;
		this.eventQueue = eventQueue;
	}	
	
	@Override
	public void run() {
		BufferedReader br = new BufferedReader(new InputStreamReader(sockIn));
		String line = new String();
		try {
			while (!this.isClosed() && ((line = br.readLine()) != null)) {
				System.out.println(line);
				this.eventQueue.put(line);
			}
			if(line == null) {
				this.eventQueue.put("failure");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	private synchronized boolean isClosed() {
		return this.closed;
	}
	
	public synchronized void close() {
		this.closed = true;
	}
}
