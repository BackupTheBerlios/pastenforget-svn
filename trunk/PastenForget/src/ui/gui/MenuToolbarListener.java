package ui.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import middleware.Middleware;
import middleware.Tools;
import ui.gui.dialog.DownloadDialog;
import ui.gui.dialog.FilterDialog;
import ui.gui.dialog.InfoDialog;
import ui.gui.dialog.IrcDownloadDialog;
import ui.gui.dialog.IrcSearchDialog;
import ui.gui.dialog.MultiDownloadDialog;
import ui.gui.dialog.PnfDownloadDialog;
import ui.gui.dialog.RsdfDownloadDialog;
import ui.gui.dialog.SearchDialog;
import ui.gui.settings.Settings;

public class MenuToolbarListener implements ActionListener {

	private GUI gui;

	public MenuToolbarListener(GUI gui) {
		this.gui = gui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();

		if ("download".equals(source)) {
			new DownloadDialog(gui);
		} else if ("multidownload".equals(source)) {
			new MultiDownloadDialog(gui);
		} else if ("pnfdownload".equals(source)) {
			new PnfDownloadDialog(gui);
		} else if ("rsdfdownload".equals(source)) {
			new RsdfDownloadDialog(gui);
		} else if ("ircdownload".equals(source)) {
			new IrcDownloadDialog(gui);
		} else if ("settings".equals(source)) {
			new Settings(gui);
		} else if ("search".equals(source)) {
			//new DllWarezDialog(gui).search();
			new SearchDialog(gui);
		} else if ("filter".equals(source)) {
			new FilterDialog(gui);
		} else if ("searchirc".equals(source)) {
			new IrcSearchDialog(gui);
		} else if ("help".equals(source)) {
			try {
				Desktop.getDesktop().open(
						new File(Tools.getProgramPath().getAbsolutePath()
								+ "/readme.txt"));
			} catch (Exception ea) {
				ea.printStackTrace();
			}
		} else if ("info".equals(source)) {
			new InfoDialog(gui);
		} else if ("quit".equals(source)) {
			Middleware.exit();
		}

	}

}
