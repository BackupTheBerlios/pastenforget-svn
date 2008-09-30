package download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import stream.Buffer;
import stream.Packet;


/**
 * Laedt eine Datei gezielt von einem Webserver.
 * 
 * @author cschaedl
 */

public class ServerDownload {
	

	private class BufferedWriter extends Thread {
		private OutputStream os = null;
		private final Buffer buf;
		
		public BufferedWriter(Buffer buf, File file) {
			this.buf = buf;
			try {
				this.os = new FileOutputStream(file);
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			Packet packet = null;
			try {
				do {
					packet = buf.read();
					os.write(packet.getBuffer(), 0, packet.getReceivedBytes());
				} while(!buf.isComplete());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Download download;
	protected ServerConnection connection;

	
	public ServerDownload( Download download ) throws Exception {
		this.download = download;
	}

	public void download() {
		try {
			Long targetFilesize;
			BufferedInputStream is;
			
			FileOutputStream os;
			int receivedBytes;
			
			this.download.setStatus("aktiv");
			this.connection = new ServerConnection( download.getDirectUrl());
			is = new BufferedInputStream( this.connection.openDownloadStream() );
			os = new FileOutputStream( download.getFileName());
			
			targetFilesize = Long.valueOf( this.connection.getHeader().get( "Content-Length" ).get(0) );
			this.download.setFileSize(targetFilesize);
			
			Buffer buf = new Buffer(is);
			BufferedWriter writer = new BufferedWriter(buf, new File(this.download.getFileName()));
			writer.start();
			
			while((receivedBytes = buf.write()) > 0) {
				this.download.setCurrentSize(this.download.getCurrentSize() + receivedBytes);
			}
			buf.setComplete();
			
			try {
				writer.join();
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted");
				e.printStackTrace();
			}
			
			os.close();
			is.close();
			this.connection.close();
			
			if( this.download == this.download.getQueue().getCurrent()) {
				this.download.getQueue().removeCurrent();
			}
		
		} catch ( MalformedURLException muex ) {
			System.out.println( "Die URL ist nicht normgerecht" );
		} catch ( IOException ioex ) {
			System.err.println( ioex.getMessage() );
			ioex.printStackTrace();
		}
		
	}
}