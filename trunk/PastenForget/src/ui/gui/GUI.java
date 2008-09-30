package ui.gui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
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

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			System.out.println("Set LookAndFeel: done");
		} catch (Exception e) {
			System.out.println("Set LookAndFeel: faild");
		}

		c.add(new ToolBar(this), BorderLayout.NORTH);
		c.add(new Downloads(this), BorderLayout.CENTER);

		this.setJMenuBar(new Menu(this));
		this.setTitle("Paste 'n' Forget");
		this.setResizable(true);
		this.setLocation(new Point(100, 100));
		this.setVisible(true);
		
		// FIXME Bei middleware.exit() laufen Threads weiter
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*
		this.addWindowListener(new WindowAdapter() {
			@SuppressWarnings("unused")
			public void windowsClosing(WindowEvent e) {
				GUI gui = (GUI) e.getSource();
				gui.middleware.exit();
			}
		});
		*/
	}

	public Middleware getMiddleware() {
		return this.middleware;
	}

	public Downloads getDownloads() {
		return this.downloads;
	}

}
