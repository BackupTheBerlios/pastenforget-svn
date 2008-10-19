package ui.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.dialog.InfoDialog;

public class HelpMenu extends JMenu implements ActionListener {

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
		menuItem.addActionListener(this);
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("informations"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_I);
		menuItem.setActionCommand("info");
		menuItem.addActionListener(this);
		this.add(menuItem);

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("help".equals(source)) {
			new InfoDialog(gui).help();
		} else if ("info".equals(source)) {
			new InfoDialog(gui).info();
		}
	}

}
