package ui.gui.dialog;

import java.io.File;

import javax.swing.JFileChooser;

import settings.Languages;

public class PathDialog {

	private File dir = null;

	public PathDialog(String dir) {
		JFileChooser fc = new JFileChooser(dir);
		fc.setDialogTitle(Languages.getTranslation("openpath"));
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(null);
		this.dir = fc.getSelectedFile();
		if (this.dir != null) {
			System.out.println("PathDialog: choose path " + this.dir);
		}
	}

	public File getDestination() {
		return this.dir;
	}

}
