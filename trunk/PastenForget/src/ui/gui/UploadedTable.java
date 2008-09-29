package ui.gui;

import download.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Rapidshare.
 * 
 * @author cpieloth
 * 
 */
public class UploadedTable extends HosterTable {

	private static final long serialVersionUID = 5217415995428180114L;

	public UploadedTable(Middleware middleware) {
		this.middleware = middleware;
		
		queue = middleware.getQueue(Hoster.UPLOADED.getKey());
		queue.addObserver(this);
		
		//cancelButton.addActionListener(this);
	}

	

}