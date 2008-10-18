package ui.gui.dialog;

import java.io.File;

import javax.swing.JFileChooser;

import settings.Languages;

public class FileDialog {

	private File file = null;

	public FileDialog(String dir) {
		JFileChooser fc = new JFileChooser(dir);
		fc.setDialogTitle(Languages.getTranslation("openfile"));
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.showOpenDialog(null);
		this.file = fc.getSelectedFile();
		if (file != null) {
			System.out.println("Choose file: " + this.file);
		}
	}

	public File getFile() {
		return file;
	}

}
