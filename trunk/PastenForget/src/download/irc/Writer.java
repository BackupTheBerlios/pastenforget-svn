package download.irc;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Writer extends Thread {
	private BufferedWriter bw = null;

	public Writer(OutputStream os) {
		this.bw = new BufferedWriter(new OutputStreamWriter(os));
	}

	public void register(String nick, String location, String fullName, String eMail, String password) {
		this.sendCommand("NICK " + nick);
		this.sendCommand("USER " + nick + " " + location + " mustermann.com :"
			+ fullName);
		try {
		// Thread.sleep(2000);
		} catch(Exception e) {
			
		}
		
	}

	public void joinChannel(String channel) {
		this.sendCommand("JOIN #" + channel);
	}

	public void sendCTCP(String botName, String command) {
		this.sendCommand("PRIVMSG " + botName + " :\u0001" + command
						+ "\u0001");
	}

	private void sendCommand(String message) {
		try {
			System.out.println("*** Send Command: " + message);
			bw.write(message + "\r\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.bw.flush();
			this.bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
