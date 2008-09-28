package gui.dialogs;

import java.io.File;

import javax.swing.JFileChooser;

public class FileDialog {

	private File file = null;

	public FileDialog(String dir) {
		JFileChooser fc = new JFileChooser(dir);
		fc.setDialogTitle("Datei öffnen");
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.showOpenDialog(null);
		file = fc.getSelectedFile();
		if (file != null) {
			System.out.println("Choose file: " + this.file);
		}
	}

	public File getFile() {
		return file;
	}

}
