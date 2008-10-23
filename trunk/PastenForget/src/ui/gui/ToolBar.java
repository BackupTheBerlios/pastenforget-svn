package ui.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import middleware.Tools;
import settings.Languages;

public class ToolBar extends JToolBar {

	private static final long serialVersionUID = 8475301015978951356L;

	private GUI gui;
	
	private JButton button;

	public ToolBar(GUI gui) {
		this.gui = gui;
		init();
	}

	private void init() {
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/default.png"));
		button.setToolTipText(Languages.getTranslation("newdownload"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("download");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/default.png"));
		button.setToolTipText(Languages.getTranslation("pnfdownload"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("pnfdownload");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/default.png"));
		button.setToolTipText(Languages.getTranslation("rsdfdownload"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("rsdfdownload");
		this.add(button);
		
		this.add(new JToolBar.Separator());
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/default.png"));
		button.setToolTipText(Languages.getTranslation("settings"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("settings");
		this.add(button);
		
		this.add(new JToolBar.Separator());

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/default.png"));
		button.setToolTipText(Languages.getTranslation("search")
				+ " (DDL-Warez)");
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("searchddl");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/default.png"));
		button.setToolTipText(Languages.getTranslation("filter")
				+ " URLs (DDL-Warez)");
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("filterddl");
		this.add(button);
		
		this.add(new JToolBar.Separator());
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/default.png"));
		button.setToolTipText(Languages.getTranslation("help"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("help");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/default.png"));
		button.setToolTipText(Languages.getTranslation("informations"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("info");
		this.add(button);
	}

}
