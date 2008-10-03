package ui.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import middleware.Middleware;
import ui.UserInterface;
import ui.gui.menubar.Menu;

/**
 * Grafische Benutzeroberflaeche.
 * 
 * @author cpieloth
 */
public class GUI extends JFrame implements UserInterface {

	Container c;

	private static final long serialVersionUID = 1L;

	private Middleware middleware;

	private Downloads downloads;

	/**
	 * Erzeugt eine grafische Benutzeroberflaeche.
	 */
	public GUI(Middleware middleware) {
		c = getContentPane();
		c.setLayout(new BorderLayout());

		this.middleware = middleware;

		this.setSize(new Dimension(700, 400));
		this.setPreferredSize(new Dimension(700, 400));

		this.setLookAndFeel(this.getMiddleware().getSettings()
				.getUserInterface());

		c.add(new ToolBar(this), BorderLayout.NORTH);
		c.add(new Downloads(this), BorderLayout.CENTER);

		this.setJMenuBar(new Menu(this));
		this.setTitle("Paste 'n' Forget");
		this.setResizable(true);
		this.setLocation(new Point(100, 100));
		this.setVisible(true);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				GUI gui = (GUI) e.getSource();
				gui.middleware.exit();
			}
		});
	}

	public Middleware getMiddleware() {
		return this.middleware;

	}

	public Downloads getDownloads() {
		return this.downloads;
	}

	public void setLookAndFeel(short i) {
		try {
			switch (i) {
			case 0:
				break;
			case 1:
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
				break;
			case 2:
				UIManager
						.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
				break;
			case 3:
				UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
				break;
			case 4:
				UIManager
				.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				break;
			default:
				break;
			}
			SwingUtilities.updateComponentTreeUI(this);
			System.out.println("Set LookAndFeel: done");
		} catch (Exception e) {
			System.out.println("Set LookAndFeel: faild");
		}

	}

}
