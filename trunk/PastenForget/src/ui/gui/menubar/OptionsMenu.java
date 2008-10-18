package ui.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import settings.Languages;
import ui.gui.GUI;
import ui.gui.settings.Settings;

public class OptionsMenu extends JMenu implements ActionListener {

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
		menuItem.addActionListener(this);
		this.add(menuItem);

		menuItem = new JMenuItem(Languages.getTranslation("quit"));
		menuItem.setMnemonic(java.awt.event.KeyEvent.VK_B);
		menuItem.setActionCommand("quit");
		menuItem.addActionListener(this);
		this.add(menuItem);

		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("settings".equals(source)) {
			new Settings(this.gui);
		} else if ("quit".equals(source)) {
			this.gui.getMiddleware().exit();
		}
	}

}
