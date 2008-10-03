package ui.gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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

	private HosterTable[] hoster;

	public Downloads(GUI gui) {
		this.gui = gui;

		tpane = new JTabbedPane();

		hoster = new HosterTable[] {
				new RapidshareTable(this.gui.getMiddleware()),
				new UploadedTable(this.gui.getMiddleware()),
				new MegauploadTable(this.gui.getMiddleware()),
				new NetloadTable(this.gui.getMiddleware()),
				new FileFactoryTable(this.gui.getMiddleware()) };

		for (int i = 0; i < hoster.length; i++) {
			tpane.add(hoster[i].getComponent(), hoster[i].getHoster());
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