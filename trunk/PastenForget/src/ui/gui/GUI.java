package ui.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import middleware.Middleware;
import middleware.Tools;
import settings.LookAndFeelEnum;
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

	private Downloads downloads;

	/**
	 * Erzeugt eine grafische Benutzeroberflaeche.
	 */
	public GUI() {
		c = getContentPane();
		c.setLayout(new BorderLayout());

		this.setMinimumSize(new Dimension(640, 480));
		this.setPreferredSize(new Dimension(800, 480));
		this.setSize(new Dimension(800, 480));

		this.setLookAndFeel(settings.Settings.getUserInterface());

		c.add(new ToolBar(this), BorderLayout.NORTH);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add(new Downloads(this));
		splitPane.add(new Log(this));
		c.add(splitPane, BorderLayout.CENTER);
		
		this.setJMenuBar(new Menu(this));
		this.setTitle("Paste 'n' Forget");
		this.setResizable(true);
		this.setLocation(Tools.getCenteredLocation(this.getPreferredSize()));
		this.setVisible(true);

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Middleware.exit();
			}
		});
	}

	public Downloads getDownloads() {
		return this.downloads;
	}

	public void setLookAndFeel(int i) {
		try {
			for (LookAndFeelEnum laf : LookAndFeelEnum.values()) {
				if (i == laf.getKey()) {
					UIManager.setLookAndFeel(laf.getClassName());
				}
			}
			SwingUtilities.updateComponentTreeUI(this);
			System.out.println("GUI.setLookAndFeel: done");
		} catch (Exception e) {
			System.out.println("GUI.setLookAndFeel: faild");
		}
	}
}
