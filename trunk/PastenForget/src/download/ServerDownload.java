package download;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;


/**
 * Laedt eine Datei gezielt von einem Webserver.
 * 
 * @author cschaedl
 */

public class ServerDownload {
	

	private class BufferedWriter extends Thread {
		private final OutputStream os;
		private final byte[] buffer;
		private final int receivedBytes;
		private final Download download;
		
		public BufferedWriter(OutputStream os, byte[] buffer, int receivedBytes, Download download) {
			this.os = os;
			this.buffer = buffer;
			this.receivedBytes = receivedBytes;
			this.download = download;
		}
		
		@Override
		public void run() {
			try {
				os.write(buffer, 0, receivedBytes);
			} catch(IOException e) {
				e.printStackTrace();
			}
			this.download.setCurrentSize(this.download.getCurrentSize() + receivedBytes);
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
			int len = 4096;
			byte buffer[] = new byte[len];
			
			this.download.setStatus("aktiv");
			this.connection = new ServerConnection( download.getDirectUrl());
			is = new BufferedInputStream( this.connection.openDownloadStream() );
			os = new FileOutputStream( download.getFileName());
			
			targetFilesize = Long.valueOf( this.connection.getHeader().get( "Content-Length" ).get(0) );
			this.download.setFileSize(targetFilesize);
			Thread writer = new Thread();
			
			while( ((receivedBytes = is.read(buffer)) > 0)) {
				if(writer.isAlive()) {
					try {
						writer.join();
					} catch(InterruptedException e) {
						System.out.println("Thread interrupted");
						e.printStackTrace();
					}
				}
				writer = new BufferedWriter(os, buffer, receivedBytes, this.download);
				writer.start();
			}
			
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