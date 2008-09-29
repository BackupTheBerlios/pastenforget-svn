package ui.gui;

import core.hoster.Hoster;
import middleware.Middleware;

/**
 * Spezielle GUI-Eigenschaften fuer Uploaded.
 * 
 * @author cpieloth
 * 
 */
public class UploadedGUI extends HosterGUI {

	private static final long serialVersionUID = 5217415995428180114L;
	
	public UploadedGUI(Middleware middleware) {
		this.middleware = middleware;
		queue = middleware.getQueue(Hoster.UPLOADED.getKey());
		queue.addObserver(this);

		hosterLabel.setText("Uploaded");
		cancelButton.addActionListener(this);
	}

}