package ui.gui.menubar;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.MenuToolbarListener;

public class DownloadMenu extends JMenu {

	private static final long serialVersionUID = -9118435082227143960L;

	private GUI gui;

	public DownloadMenu(GUI gui) {
		this.setText(Languages.getTranslation("Download"));
		this.gui = gui;
		this.setMnemonic(KeyEvent.VK_D);
		init();
	}

	private void init() {
		JMenuItem menuItem = new JMenuItem(Languages
				.getTranslation("NewDownload"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_N);
		menuItem.setActionCommand("download");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);
		
		menuItem = new JMenuItem(Languages
				.getTranslation("Multi-Download"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_M);
		menuItem.setActionCommand("multidownload");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("PnF-Download"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_P);
		menuItem.setActionCommand("pnfdownload");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);
		
		menuItem = new JMenuItem(Languages.getTranslation("RSDF-Download"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_R);
		menuItem.setActionCommand("rsdfdownload");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);
		
		menuItem = new JMenuItem(Languages.getTranslation("IRC-Download"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_I);
		menuItem.setActionCommand("ircdownload");
		menuItem.addActionListener(new MenuToolbarListener(gui));
		this.add(menuItem);

		this.setVisible(true);
	}

}
