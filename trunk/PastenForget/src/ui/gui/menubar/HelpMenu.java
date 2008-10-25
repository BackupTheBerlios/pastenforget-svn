package ui.gui.menubar;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.MenuToolbarListener;

public class HelpMenu extends JMenu {

	private static final long serialVersionUID = -9118435082227143960L;

	private GUI gui;

	public HelpMenu(GUI gui) {
		this.gui = gui;
		this.setText(Languages.getTranslation("help"));
		this.setMnemonic(KeyEvent.VK_H);
		init();
	}

	private void init() {
		JMenuItem menuItem = new JMenuItem(Languages.getTranslation("help"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_H);
		menuItem.setActionCommand("help");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("information"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_I);
		menuItem.setActionCommand("info");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		this.setVisible(true);
	}

}
