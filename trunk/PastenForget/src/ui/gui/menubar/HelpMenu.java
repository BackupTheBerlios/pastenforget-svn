package ui.gui.menubar;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ui.gui.dialog.InfoDialog;

public class HelpMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = -9118435082227143960L;

	public HelpMenu() {
		this.setText("Hilfe");
		this.setMnemonic(KeyEvent.VK_H);
		init();
	}

	private void init() {
		JMenuItem menuItem = new JMenuItem("Hilfe");
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_H);
		menuItem.setActionCommand("help");
		menuItem.addActionListener(this);
		this.add(menuItem);

		menuItem = new JMenuItem("Info");
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
			new InfoDialog().help();
		} else if ("info".equals(source)) {
			new InfoDialog().info();
		}
	}

}
