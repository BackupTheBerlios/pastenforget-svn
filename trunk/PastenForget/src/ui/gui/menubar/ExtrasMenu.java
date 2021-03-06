package ui.gui.menubar;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.MenuToolbarListener;

public class ExtrasMenu extends JMenu {

	private static final long serialVersionUID = -9118435082227143960L;

	private GUI gui;

	public ExtrasMenu(GUI gui) {
		this.setText(Languages.getTranslation("Extras"));
		this.gui = gui;
		this.setMnemonic(KeyEvent.VK_E);
		init();
	}

	private void init() {
		JMenuItem menuItem = new JMenuItem(Languages.getTranslation("Search"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_S);
		menuItem.setActionCommand("search");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("Filter")
				+ " URLs");
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_F);
		menuItem.setActionCommand("filter");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);
		
		menuItem = new JMenuItem(Languages.getTranslation("Search")
				+ " (IRC Packetnews)");
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_I);
		menuItem.setActionCommand("searchirc");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		this.setVisible(true);
	}

}
