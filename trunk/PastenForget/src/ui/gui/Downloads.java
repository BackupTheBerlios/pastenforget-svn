package ui.gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import download.hoster.Hoster;

/**
 * Erzeugt die Kontrollelemente fuer die Downloads.
 * 
 * @author cpieloth
 * 
 */
public class Downloads extends JPanel {

	private static final long serialVersionUID = 5217415995428180114L;

	private GUI gui;

	private JTabbedPane tpane;

	public Downloads(GUI gui) {
		this.gui = gui;

		tpane = new JTabbedPane();
		for (Hoster hoster : Hoster.values()) {
			if (hoster.getKey() > -1) {
				tpane.add(new HosterTable(this.gui.getMiddleware(), hoster),
						hoster.getName());
			}
		}

		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				JPanel panel = (JPanel) e.getSource();
				tpane.setSize(panel.getWidth() - 10, panel.getHeight() - 10);
				tpane.setPreferredSize(new Dimension(panel.getWidth() - 10,
						panel.getHeight() - 10));
				tpane.setLocation(5, 5);
			}
		});

		this.add(tpane);
	}

}