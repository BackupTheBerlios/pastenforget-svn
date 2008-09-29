package download;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
//import java.util.Formatter;


/**
 * Laedt eine Datei gezielt von einem Webserver.
 * 
 * @author cschaedl
 */

public class ServerDownload {
	
/*-------------------------------------------------------------------------------	

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

-------------------------------------------------------------------------------*/	

	private Download download;
	protected ServerConnection connection;

	
	public ServerDownload( Download download ) throws Exception {
		this.download = download;
	}

	public void download() {
		try {
			Long currentFilesize = new Long(0);
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
			
			while( ((receivedBytes = is.read(buffer)) > 0) && (this.download.stopThread.isStopped() == false) ) {
				os.write( buffer, 0, receivedBytes );
				currentFilesize += receivedBytes;
				
				this.download.setCurrentSize(currentFilesize);
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