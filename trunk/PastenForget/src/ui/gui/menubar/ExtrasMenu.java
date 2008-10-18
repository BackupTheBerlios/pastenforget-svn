package ui.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.dialog.ExtrasDialog;

public class ExtrasMenu extends JMenu implements ActionListener {

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
		menuItem.setActionCommand("search");
		menuItem.addActionListener(this);
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("filter")
				+ " URLs (DDL-Warez)");
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_F);
		menuItem.setActionCommand("filter");
		menuItem.addActionListener(this);
		this.add(menuItem);

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("search".equals(source)) {
			new ExtrasDialog(this.gui).search();
		} else if ("filter".equals(source)) {
			new ExtrasDialog(this.gui).filter();
		}
	}

}
