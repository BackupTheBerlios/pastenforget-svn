package gui;

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

	public Downloads(GUI gui) {
		this.gui = gui;

		tpane = new JTabbedPane();

		tpane.addTab("Rapidshare",
				new RapidshareTable(this.gui.getMiddleware()));
		tpane.addTab("Uploaded", new UploadedGUI(this.gui.getMiddleware())
				.getComponent(0));
		tpane.addTab("Megaupload", new MegauploadGUI(this.gui.getMiddleware())
				.getComponent(0));
		tpane.addTab("Netload", new NetloadGUI(this.gui.getMiddleware())
				.getComponent(0));
		
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				JPanel panel = (JPanel) e.getSource();
				tpane.setSize(panel.getWidth() - 10, panel.getHeight() - 10);
				tpane.setPreferredSize(new Dimension(panel.getWidth() - 10, panel.getHeight() - 10));
				tpane.setLocation(5, 5);
			}
		});

		this.add(tpane);
	}

}