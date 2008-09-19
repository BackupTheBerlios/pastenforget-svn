package gui;

import core.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Megaupload.
 * 
 * @author cpieloth
 * 
 */
public class MegauploadGUI extends HosterGUI {

	private static final long serialVersionUID = 5217415995428180114L;

	public MegauploadGUI(Middleware middleware) {
		this.middleware = middleware;

		queue = middleware.getQueue(Hoster.MEGAUPLOAD.getKey());
		queue.addObserver(this);
		
		hosterLabel.setText("Megaupload");
		cancelButton.addActionListener(this);
	}

}