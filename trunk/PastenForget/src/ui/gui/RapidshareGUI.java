package ui.gui;

import download.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Rapidshare.
 * 
 * @author cpieloth
 * 
 */
public class RapidshareGUI extends HosterGUI {

	private static final long serialVersionUID = 5217415995428180114L;

	public RapidshareGUI(Middleware middleware) {
		this.middleware = middleware;
		
		queue = middleware.getQueue(Hoster.RAPIDSHARE.getKey());
		queue.addObserver(this);

		hosterLabel.setText("Rapidshare");
		cancelButton.addActionListener(this);
	}

	

}