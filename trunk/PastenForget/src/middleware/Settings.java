package middleware;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Settings {
	private File destination = null;
	
	private File destinationDllwarez = null;
	
	private short userInterface = 1;
	
	public void setDestination(File destination) {
		this.destination = destination;
	}
	
	public File getDestination() {
		return this.destination;
	}
	
	public void setDestinatioDllwarez(File destinatioDllwarez) {
		this.destinationDllwarez = destinatioDllwarez;
	}

	public File getDestinatioDllwarez() {
		return destinationDllwarez;
	}
	
    public void setUserInterface(short userInterface) {
		this.userInterface = userInterface;
	}

	public short getUserInterface() {
		return userInterface;
	}

	public void save() { 
        try { 
        	XMLEncoder e = new XMLEncoder(
                    new BufferedOutputStream(
                        new FileOutputStream("settings.xml")));
        	e.writeObject(destination.getPath());
        	e.close(); 
        } 
        catch(Exception e) { e.printStackTrace();  } 
    } 
    
    public void read() { 
    	try { 
            XMLDecoder de = new XMLDecoder(new BufferedInputStream(new FileInputStream("settings.xml"))); 
            this.destination = new File((String)de.readObject());
            de.close(); 
        } 
        catch(Exception e) { e.printStackTrace();  } 

    }
    
    public static void main(String[] args) {
    	Settings settings = new Settings();
    	settings.setDestination(new File("/home/christopher/share"));
    	settings.save();
    	settings = new Settings();
    	settings.read();
    	System.out.println(settings.getDestination().toString());
    }
}
