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
		button.setToolTipText(Languages.getTranslation("newdownload"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("download");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/pnfdownload.png"));
		button.setToolTipText(Languages.getTranslation("pnfdownload"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("pnfdownload");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/rsdfdownload.png"));
		button.setToolTipText(Languages.getTranslation("rsdfdownload"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("rsdfdownload");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/newdownload.png"));
		button.setToolTipText(Languages.getTranslation("ircdownload"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("ircdownload");
		this.add(button);
		
		this.add(new JToolBar.Separator());
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/settings.png"));
		button.setToolTipText(Languages.getTranslation("settings"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("settings");
		this.add(button);
		
		this.add(new JToolBar.Separator());

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/search.png"));
		button.setToolTipText(Languages.getTranslation("search"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("search");
		this.add(button);

		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/filter.png"));
		button.setToolTipText(Languages.getTranslation("filter"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("filter");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/search.png"));
		button.setToolTipText(Languages.getTranslation("search")
				+ " (IRC Packetnews)");
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("searchirc");
		this.add(button);
		
		this.add(new JToolBar.Separator());
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/help.png"));
		button.setToolTipText(Languages.getTranslation("help"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("help");
		this.add(button);
		
		button = new JButton();
		button.setIcon(new ImageIcon(Tools.getProgramPath().getAbsolutePath()
				+ "/images/info.png"));
		button.setToolTipText(Languages.getTranslation("information"));
		button.addActionListener(new MenuToolbarListener(gui));
		button.setActionCommand("info");
		this.add(button);
	}

}
