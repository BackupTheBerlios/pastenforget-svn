package ui.gui;

import download.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Rapidshare.
 * 
 * @author cpieloth
 * 
 */
public class RapidshareTable extends HosterTable {

	private static final long serialVersionUID = 5217415995428180114L;

	public RapidshareTable(Middleware middleware) {
		this.middleware = middleware;
		
		queue = middleware.getQueue(Hoster.RAPIDSHARE.getKey());
		queue.addObserver(this);
		
		//cancelButton.addActionListener(this);
	}

	

}