package ui.gui.dialog;


import java.awt.event.ActionEvent;
import java.io.File;

import ui.gui.GUI;

public class SettingsDialog extends Dialog {

	private static final long serialVersionUID = -7459402167878262668L;

	private File destination = null;

	public SettingsDialog(GUI gui) {
		this.gui = gui;
		init();
	}

	private void init() {
		this.setTitle("Einstellungen");

		label.setText("Pfad:");
		browse.setVisible(true);
		confirm.setText("OK");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("cancel".equals(source)) {
			this.dispose();
		} else if ("confirm".equals(source)) {
			gui.getMiddleware().getSettings().setDestination(destination);
			this.dispose();
		} else if ("path".equals(source)) {
			destination = new PathDialog().getDestination();
			if (destination != null) {
				textField.setText(destination.getPath());
			}
		}

	}
}
