package core;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Formatter;

/**
 * Laedt eine Datei gezielt von einem Webserver.
 * 
 * @author cschaedl
 */

public class ServerDownload {
	
/*-------------------------------------------------------------------------------*/	

	private class DisplayThread extends Thread {
		private Long currentFilesize;
		private Long targetFilesize;
		private Download download;
		
		public DisplayThread() { }
		
		public DisplayThread( Long currentFilesize, Long targetFilesize, Download download ) {
			this.currentFilesize = currentFilesize;
			this.targetFilesize = targetFilesize;
			this.download = download;
		}
		
		public void run() {
			Double curFilesize = new Long( currentFilesize / 1024 ).doubleValue();
			Double serFilesize = new Long( targetFilesize / 1024 ).doubleValue();
			Double prozent = curFilesize * 100 / serFilesize;
			this.download.setStatus( new Formatter().format( "%1.1f", prozent ).toString() + "%" );
		}
	}

/*-------------------------------------------------------------------------------*/	

	private String url;
	private String filename;
	private Download download;
	protected ServerConnection connection;

	
	public ServerDownload( String url, String filename,Download download ) throws Exception {
		this.filename = filename; 
		this.download = download;
		this.url = url;
	}

	public void run() {
		try {
			DisplayThread dt = new DisplayThread();
			Long currentFilesize = new Long(0);
			Long targetFilesize;
			BufferedInputStream is;
			
			FileOutputStream os;
			int receivedBytes;
			int len = 4096;
			byte buffer[] = new byte[len];
			
			this.connection = new ServerConnection( this.url );
			is = new BufferedInputStream( this.connection.openDownloadStream() );
			os = new FileOutputStream( this.filename );
			targetFilesize = Long.valueOf( this.connection.getHeader().get( "Content-Length" ).get(0) );
			
			while( ((receivedBytes = is.read(buffer)) > 0) && (this.download.stopThread.isStopped() == false) ) {
				os.write( buffer, 0, receivedBytes );
				currentFilesize += receivedBytes;
				
				if( dt.isAlive() == false ) {
					dt = new DisplayThread( currentFilesize, targetFilesize, this.download );
					dt.start();	
				}	
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