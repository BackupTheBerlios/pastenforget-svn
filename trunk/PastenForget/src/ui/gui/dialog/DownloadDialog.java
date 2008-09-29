package ui.gui.dialog;


import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;

import ui.gui.GUI;

public class DownloadDialog extends Dialog {

	private static final long serialVersionUID = -7459402167878262668L;

	public DownloadDialog(GUI gui) {
		this.gui = gui;
		init();
	}

	private void init() {
		this.setTitle("Download");

		label.setText("URL:");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("cancel".equals(source)) {
			this.dispose();
		} else if ("confirm".equals(source)) {
			try {
				gui.getMiddleware().download(new URL(this.textField.getText()));
			} catch (MalformedURLException e1) {
				System.out.println("Start download: wrong URL format");
				e1.printStackTrace();
			}
			this.dispose();
		}
	}
}
