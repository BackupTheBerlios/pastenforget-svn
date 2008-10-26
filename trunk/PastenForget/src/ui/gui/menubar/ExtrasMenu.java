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
		this.setText(Languages.getTranslation("extras"));
		this.gui = gui;
		this.setMnemonic(KeyEvent.VK_E);
		init();
	}

	private void init() {
		JMenuItem menuItem = new JMenuItem(Languages.getTranslation("search")
				+ " (DDL-Warez)");
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_S);
		menuItem.setActionCommand("searchddl");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("filter")
				+ " URLs (DDL-Warez)");
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_F);
		menuItem.setActionCommand("filterddl");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);
		
		menuItem = new JMenuItem(Languages.getTranslation("search")
				+ " (IRC)");
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_I);
		menuItem.setActionCommand("searchirc");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		this.setVisible(true);
	}

}
