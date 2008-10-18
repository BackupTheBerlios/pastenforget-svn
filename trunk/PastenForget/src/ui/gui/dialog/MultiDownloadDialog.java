package ui.gui.dialog;

import java.awt.event.ActionEvent;
import java.io.File;

import settings.Languages;
import ui.gui.GUI;

public class MultiDownloadDialog extends Dialog {

	private static final long serialVersionUID = -7459402167878262668L;

	private File file = null;

	public MultiDownloadDialog(GUI gui) {
		this.gui = gui;
		init();
	}

	private void init() {
		this.setTitle(Languages.getTranslation("multidownload"));

		label.setText(Languages.getTranslation("file") + ":");
		if (this.gui.getMiddleware().getSettings().getDdlDirectory() != null) {
			textField.setText(this.gui.getMiddleware().getSettings().getDdlDirectory().toString());
		}
		browse.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("cancel".equals(source)) {
			this.dispose();
		} else if ("confirm".equals(source)) {
			gui.getMiddleware().load(file);
			this.dispose();
		} else if ("path".equals(source)) {
			file = new FileDialog(this.textField.getText()).getFile();
			if (file != null) {
				textField.setText(file.getPath());
			}
		}

	}
}
