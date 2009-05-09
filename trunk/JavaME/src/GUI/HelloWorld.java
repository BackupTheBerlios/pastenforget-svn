package GUI;
import java.io.IOException;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import Collections.StringWrapper;
import IO.File;

public class HelloWorld extends MIDlet {

	private List list;
	
	public HelloWorld() {
		try {
			File file = new File("test3.txt");
			StringWrapper fileContent = new StringWrapper(file.readText());
			list = new List("", List.IMPLICIT, fileContent.split("\n"), null);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub

	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(list);
	}

}
