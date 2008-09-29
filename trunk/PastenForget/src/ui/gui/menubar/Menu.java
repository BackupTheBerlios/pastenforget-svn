package ui.gui.menubar;


import javax.swing.JMenuBar;

import ui.gui.GUI;

/**
 * Erzeugt ein Menueobjekt.
 * 
 * @author cpieloth
 */
public class Menu extends JMenuBar {

	private static final long serialVersionUID = -3286359942252800423L;

	private GUI gui;
	
	public Menu(GUI gui) {
		this.gui = gui;
		init();
	}

	protected void init() {
		this.add(new DownloadMenu(this.gui));
		this.add(new OptionsMenu(this.gui));
		this.add(new HelpMenu());
		this.setVisible(true);
	}

}
