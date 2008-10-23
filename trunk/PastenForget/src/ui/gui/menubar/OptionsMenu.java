package ui.gui.menubar;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.MenuToolbarListener;

public class OptionsMenu extends JMenu {

	private static final long serialVersionUID = -9118435082227143960L;

	private GUI gui;

	public OptionsMenu(GUI gui) {
		this.setText(Languages.getTranslation("options"));
		this.gui = gui;
		this.setMnemonic(KeyEvent.VK_O);
		init();
	}

	private void init() {
		JMenuItem menuItem = new JMenuItem(Languages.getTranslation("settings"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_E);
		menuItem.setActionCommand("settings");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("quit"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_B);
		menuItem.setActionCommand("quit");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		this.setVisible(true);
	}

}
