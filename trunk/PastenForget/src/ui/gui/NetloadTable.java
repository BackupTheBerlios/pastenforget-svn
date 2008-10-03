package ui.gui;

import download.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Rapidshare.
 * 
 * @author cpieloth
 * 
 */
public class NetloadTable extends HosterTable {

	private static final long serialVersionUID = 5217415995428180114L;

	public NetloadTable(Middleware middleware) {
		this.middleware = middleware;
		name = "Netload*";
		
		queue = middleware.getQueue(Hoster.NETLOAD.getKey());
		queue.addObserver(this);
		
		//cancelButton.addActionListener(this);
	}

	

}