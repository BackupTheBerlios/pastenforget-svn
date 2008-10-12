package ui.gui;

import javax.swing.JTabbedPane;

import download.hoster.HosterEnum;

/**
 * Erzeugt die Kontrollelemente fuer die Downloads.
 * 
 * @author cpieloth
 * 
 */
public class Downloads extends JTabbedPane {

	private static final long serialVersionUID = 5217415995428180114L;

	private GUI gui;

	public Downloads(GUI gui) {
		this.gui = gui;

		for (HosterEnum hoster : HosterEnum.values()) {
			if (hoster.getKey() > -1) {
				this.add(new HosterTable(this.gui.getMiddleware(), hoster),
						hoster.getName());
			}
		}
	}

}