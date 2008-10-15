package ui.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import middleware.Tools;
import ui.gui.dialog.DownloadDialog;
import ui.gui.dialog.ExtrasDialog;
import ui.gui.dialog.MultiDownloadDialog;
import ui.gui.settings.Settings;

public class ToolBar extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 8475301015978951356L;
	
	private GUI gui;

	public ToolBar(GUI gui) {
		this.gui = gui;
		init();
	}

	protected void init() {
		JButton button;

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath() + "/images/default.png"));
		button.setToolTipText("Neuer Download");
		button.addActionListener(this);
		button.setActionCommand("download");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath() + "/images/default.png"));
		button.setToolTipText("Multidownload");
		button.addActionListener(this);
		button.setActionCommand("multidownload");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath() + "/images/default.png"));
		button.setToolTipText("Einstellungen");
		button.addActionListener(this);
		button.setActionCommand("settings");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath() + "/images/default.png"));
		button.setToolTipText("Suche (DDL-Warez)");
		button.addActionListener(this);
		button.setActionCommand("searchddl");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath() + "/images/default.png"));
		button.setToolTipText("Filter URLs (DDL-Warez)");
		button.addActionListener(this);
		button.setActionCommand("filterddl");
		this.add(button);
	}

	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("download".equals(source)) {
			new DownloadDialog(gui);
		} else if ("multidownload".equals(source)) {
			new MultiDownloadDialog(gui);
		} else if ("settings".equals(source)) {
			new Settings(gui);
		}else if ("searchddl".equals(source)) {
			new ExtrasDialog(gui).search();
		}else if ("filterddl".equals(source)) {
			new ExtrasDialog(gui).filter();
		}
	}

}
