package ui.gui.menubar;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import middleware.Middleware;
import settings.Languages;

public class TrayMenu extends PopupMenu {
	private static final long serialVersionUID = -1855940159236124866L;

	public TrayMenu() {
		init();
	}

	private void init() {
		MenuItem menuItem = new MenuItem(Languages.getTranslation("Show"));
		menuItem.setActionCommand("show");
		menuItem.addActionListener(new TrayListener());
		this.add(menuItem);
		
		menuItem = new MenuItem(Languages.getTranslation("Hide"));
		menuItem.setActionCommand("hide");
		menuItem.addActionListener(new TrayListener());
		this.add(menuItem);
		
		menuItem = new MenuItem(Languages.getTranslation("Quit"));
		menuItem.setActionCommand("quit");
		menuItem.addActionListener(new TrayListener());
		this.add(menuItem);
	}

	private class TrayListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String source = e.getActionCommand();
			if ("show".equals(source)) {
				Middleware.getUI().showWindows();
			} else if ("hide".equals(source)) {
				Middleware.getUI().hideWindows();
			} else if ("quit".equals(source)) {
				Middleware.exit();
			}
		}

	}
}
