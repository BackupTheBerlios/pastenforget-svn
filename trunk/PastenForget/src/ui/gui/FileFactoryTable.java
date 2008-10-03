package ui.gui;

import download.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Rapidshare.
 * 
 * @author cpieloth
 * 
 */
public class FileFactoryTable extends HosterTable {

	private static final long serialVersionUID = 5217415995428180114L;

	public FileFactoryTable(Middleware middleware) {
		this.middleware = middleware;
		name = "FileFactory*";
		
		queue = middleware.getQueue(Hoster.FILEFACTORY.getKey());
		queue.addObserver(this);
		
		//cancelButton.addActionListener(this);
	}

	

}