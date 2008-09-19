package gui;

import core.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Megaupload.
 * 
 * @author cpieloth
 * 
 */
public class NetloadGUI extends HosterGUI {

	private static final long serialVersionUID = 5217415995428180114L;

	public NetloadGUI(Middleware middleware) {
		this.middleware = middleware;

		queue = middleware.getQueue(Hoster.NETLOAD.getKey());
		queue.addObserver(this);
		
		hosterLabel.setText("Netload");
		cancelButton.addActionListener(this);
	}

}