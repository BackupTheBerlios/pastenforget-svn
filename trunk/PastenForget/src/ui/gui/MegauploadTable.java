package ui.gui;

import download.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Rapidshare.
 * 
 * @author cpieloth
 * 
 */
public class MegauploadTable extends HosterTable {

	private static final long serialVersionUID = 5217415995428180114L;

	public MegauploadTable(Middleware middleware) {
		this.middleware = middleware;
		
		queue = middleware.getQueue(Hoster.MEGAUPLOAD.getKey());
		queue.addObserver(this);
		
		//cancelButton.addActionListener(this);
	}

	

}