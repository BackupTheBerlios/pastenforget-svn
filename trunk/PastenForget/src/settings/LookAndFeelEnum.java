package settings;

import javax.swing.UIManager;

/**
 * Aufzaehlung aller LookAndFeels.
 * 
 * @author executor
 * 
 */
public enum LookAndFeelEnum {
	STANDARD(0, "Standard", ""), CONSOLE(1, "console", ""), OS(
			2, "os", ""), METAL(3, "Metal",
			"javax.swing.plaf.metal.MetalLookAndFeel"), MOTIF(4, "Motif",
			"com.sun.java.swing.plaf.motif.MotifLookAndFeel"), GTK(5, "GTK",
			"com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

	private final int KEY;

	private final String LABEL;

	private final String CLASSNAME;

	LookAndFeelEnum(int key, String label, String className) {
		this.KEY = key;
		this.LABEL = label;
		if (key == 2 || key == 0) {
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
