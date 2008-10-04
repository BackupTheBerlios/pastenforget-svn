package settings;

import javax.swing.UIManager;

/**
 * Aufzaehlung aller LookAndFeels.
 * 
 * @author executor
 * 
 */
public enum LookAndFeelEnum {
	CONSOLE(0, "Konsole (Nicht unterstützt!)", ""), OS(1, "Betriebssystem", ""), METAL(
			2, "Metal", "javax.swing.plaf.metal.MetalLookAndFeel"), MOTIF(3,
			"Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel"), GTK(4,
			"GTK", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

	private final int KEY;

	private final String LABEL;

	private final String CLASSNAME;

	LookAndFeelEnum(int key, String label, String className) {
		this.KEY = key;
		this.LABEL = label;
		if (key == 1) {
			this.CLASSNAME = UIManager.getSystemLookAndFeelClassName();
		} else {
			this.CLASSNAME = className;
		}
	}

	public int getKey() {
		return KEY;
	}

	public String getLabel() {
		return LABEL;
	}

	public String getClassName() {
		return CLASSNAME;
	}
}
