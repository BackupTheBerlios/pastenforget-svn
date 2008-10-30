package ui.gui;

import javax.swing.JTabbedPane;

import download.HosterEnum;

/**
 * Erzeugt die Kontrollelemente fuer die Downloads.
 * 
 * @author executor
 * 
 */
public class Downloads extends JTabbedPane {

	private static final long serialVersionUID = 5217415995428180114L;

	public Downloads(GUI gui) {

		for (HosterEnum hoster : HosterEnum.values()) {
			if (hoster.getKey() > -1) {
				this.add(new HosterTable(gui, hoster), hoster.getName());
			}
		}
	}

}