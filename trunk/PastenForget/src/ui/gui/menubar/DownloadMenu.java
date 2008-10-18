package ui.gui.menubar;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.dialog.DownloadDialog;
import ui.gui.dialog.MultiDownloadDialog;

public class DownloadMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = -9118435082227143960L;

	private GUI gui;
	
	public DownloadMenu(GUI gui) {
		this.setText(Languages.getTranslation("download"));
		this.gui = gui;
		this.setMnemonic(KeyEvent.VK_D);
		init();
	}

	private void init() {
		JMenuItem menuItem = new JMenuItem(Languages.getTranslation("newdownload"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_N);
		menuItem.setActionCommand("download");
		menuItem.addActionListener(this);
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("multidownload"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_M);
		menuItem.setActionCommand("multidownload");
		menuItem.addActionListener(this);
		this.add(menuItem);
		
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("download".equals(source)) {
			new DownloadDialog(gui);
		} else if ("multidownload".equals(source)) {
			new MultiDownloadDialog(gui);
		} 
	}

}
