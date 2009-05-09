package GUI;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import Collections.LinkedList;
import Parser.Tag;

public class HelloWorld extends MIDlet {

	private List list;

	public LinkedList split(String seperator, String input) {
		String text = new String(input);
		LinkedList linkedList = new LinkedList();
		int index;
		while ((index = text.indexOf('\n')) != -1) {
			linkedList.add(text.substring(0, index));
			text = text.substring(index + 1);
		}
		if (text.length() > 0) {
			linkedList.add(text);
		}
		return linkedList;
	}

	public String readFile() {
		try {
			InputStream in = HelloWorld.class.getResourceAsStream("test3.txt");
			InputStreamReader rs = new InputStreamReader(in);
			StringBuffer sb = new StringBuffer();
			char[] buffer = new char[3];
			while (rs.read(buffer) > 0) {
				sb.append(buffer);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public LinkedList readTagsFromString(String input) {
		Tag tag = new Tag(input);
		LinkedList lList = tag.getAllTags();
		for (int i = 0; i < lList.size(); i++) {
			list.append((String) lList.get(i), null);
		}
		return lList;
	}

	public HelloWorld() {
		list = new List("", List.IMPLICIT);
		try {
			HttpConnection conn = (HttpConnection)Connector.open("http://www.handballwoelfe.de/Pages/Index.php");
			InputStream in = conn.openInputStream();
			InputStreamReader rs = new InputStreamReader(in);
			StringBuffer sb = new StringBuffer();
			char[] buffer = new char[3];
			while (rs.read(buffer) > 0) {
				sb.append(buffer);
			}
			System.out.println(sb.toString());
			LinkedList lList = this.readTagsFromString(sb.toString());
			for(int i = 0; i < lList.size(); i++) {
				list.append((String)lList.get(i), null);
			}
			
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
