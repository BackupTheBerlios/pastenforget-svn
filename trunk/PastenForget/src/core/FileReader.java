package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
	private final File file;
	
	public FileReader(File that) {
		file = that;
	}
	
	private BufferedReader openFile() {
		FileInputStream fis;
		try {
			fis = new FileInputStream(file.getPath());
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			return br;
		} catch(IOException ioe) {
			System.out.println("File nicht gefunden");
			return null;
		}
	}
	
	public List<String> getLinkList() {
		BufferedReader br = openFile();
		List<String> links = new ArrayList<String>();
		String readLine = new String();
		try {
			while((readLine = br.readLine()) != null) {
				links.add(readLine);
			}
			return links;
		} catch(IOException ioe) {
			System.out.println("kein Inputstream");
			return null;
		}
		
	}
	
}
