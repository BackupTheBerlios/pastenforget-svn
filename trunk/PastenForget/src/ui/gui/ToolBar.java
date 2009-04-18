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
				+ "/images/newdownload.png"));
		button.setToolTipText(Languages.getTranslation("NewDownload"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("download");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/multidownload.png"));
		button.setToolTipText(Languages.getTranslation("Multi-Download"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("multidownload");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/rsdfdownload.png"));
		button.setToolTipText(Languages.getTranslation("RSDF-Download"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("rsdfdownload");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/newdownload.png"));
		button.setToolTipText(Languages.getTranslation("IRC-Download"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("ircdownload");
		this.add(button);
		
		this.add(new JToolBar.Separator());
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/settings.png"));
		button.setToolTipText(Languages.getTranslation("Settings"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("settings");
		this.add(button);
		
		this.add(new JToolBar.Separator());

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/search.png"));
		button.setToolTipText(Languages.getTranslation("Search"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("search");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/filter.png"));
		button.setToolTipText(Languages.getTranslation("Filter"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("filter");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/search.png"));
		button.setToolTipText(Languages.getTranslation("Search")
				+ " (IRC Packetnews)");
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("searchirc");
		this.add(button);
		
		this.add(new JToolBar.Separator());
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/help.png"));
		button.setToolTipText(Languages.getTranslation("Help"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("help");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/info.png"));
		button.setToolTipText(Languages.getTranslation("Information"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("info");
		this.add(button);
	}

}
