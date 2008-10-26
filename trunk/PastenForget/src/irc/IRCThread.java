package irc;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class IRCThread implements Runnable {
	private PrintStream out = System.out;
	private String ircServer;
	private String nickName = "nickname";
	private String fullName = "surname lastname";
	private String password = "password";
	private String eMail = "nick.name@muster.ru";
	private final String location = "At-home";
	private String ircChannel;
	private String botName;
	private String packageNr;
	private Integer port = new Integer(6667);

	private Reader reader;
	private Writer writer;
	private Socket socket;
	private BlockingQueue<String> eventQueue = new SynchronousQueue<String>();;

	public void setIrcServer(String ircServer) {
		this.ircServer = ircServer;
	}

	public void setIrcChannel(String ircChannel) {
		this.ircChannel = ircChannel;
	}

	public void setBotName(String botName) {
		this.botName = botName;
	}

	public void setPackageNr(String packageNr) {
		this.packageNr = packageNr;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	private void prepareConnection() {
		try {
			out.println("*** Connecting to " + this.ircServer);
			this.socket = new Socket(this.ircServer, this.port);
			eventQueue = new SynchronousQueue<String>();
			reader = new Reader(socket.getInputStream(), this.eventQueue);
			writer = new Writer(socket.getOutputStream());
			new Thread(reader).start();
		} catch (IOException e) {
			this.prepareConnection();
		}
	}

	// TODO All Slots Full
	// TODO Connection refresh
	public void download() {
		DCCDownload download = null;
		try {
			this.writer.register(this.nickName, this.location, this.fullName,
					this.eMail, this.password);
			boolean loggedIn = false;
			boolean channelJoined = false;
			boolean inMainQueue = false;
			DCCPackage downloadPackage = null;
			String message = new String();
			boolean stayActive = true;
			do {
				message = this.eventQueue.take();
				if ((message.indexOf("Undertaker") != -1) && (!loggedIn)) {
					// ==========================================================
					out.println("*** Connected to Server " + this.ircServer);
					this.writer.joinChannel(this.ircChannel);
					loggedIn = true;
					// ==========================================================
				} else if (((message.indexOf(this.ircChannel.toUpperCase()) != -1) || (message
						.indexOf(this.ircChannel.toLowerCase()) != -1))
						&& (!channelJoined)) {
					// ==========================================================
					Thread.sleep(2000);
					out.println("*** Connected to Channel " + this.ircChannel);
					if (!inMainQueue) {
						this.writer.sendCTCP(botName, "xdcc send #"
								+ this.packageNr);
					}
					channelJoined = true;
					// ==========================================================
				} else if ((message.indexOf("DCC SEND") != -1)
						&& (message.indexOf("XDCC SEND") == -1)) {
					// ==========================================================
					out.println("*** Bot " + this.botName
							+ " sends Download Package ");
					downloadPackage = evaluateDownloadMessage(message);
					out.println("*** Sending Accept to " + this.botName
							+ " for Send");
					writer.sendCTCP(this.botName, "dcc accept "
							+ downloadPackage.getFileName() + " "
							+ downloadPackage.getPort() + " 0");
					String fileName = downloadPackage.getFileName();
					File file = new File(fileName);
					Long downloadedFileSize = new Long(0);
					if (file.exists()) {
						downloadedFileSize = file.length();
					}
					downloadPackage.setDownloadedFileSize(downloadedFileSize);
					Thread.sleep(3000);
					out.println("*** Sending Resume ["
							+ downloadPackage.getFileName() + " "
							+ downloadPackage.getPort() + " "
							+ downloadPackage.getDownloadedFileSize() + "] to"
							+ this.botName);
					writer.sendCTCP(this.botName, "dcc resume "
							+ downloadPackage.getFileName() + " "
							+ downloadPackage.getPort() + " "
							+ downloadPackage.getDownloadedFileSize());
					Thread.sleep(3000);
					out.println("*** Sending Accept to " + this.botName
							+ " for Resume");
					writer.sendCTCP(this.botName, "dcc accept "
							+ downloadPackage.getFileName() + " "
							+ downloadPackage.getPort() + " "
							+ downloadPackage.getDownloadedFileSize());
					download = new DCCDownload(downloadPackage, this.eventQueue);
					Thread dl = new Thread(download);
					dl.start();
					// ==========================================================
				} else if (message.indexOf("XDCC SEND denied") != -1) {
					out.println("*** Transfer denied");
					stayActive = false;
				} else if ((message.indexOf("You already requested that pack") != -1)
						|| (message.indexOf("Du hast diese Datei bereits angefordert") != -1)) {
					// ==========================================================
					out.println("*** Package already requested");
					this.writer.sendCTCP(botName, "dcc cancel");
					// ==========================================================
				} else if(message.indexOf("Closing Connection: DCC Timeout") != -1) { 
					this.writer.sendCTCP(botName, "xdcc send #"
							+ this.packageNr);
				} else if (message.indexOf("You can only have") != -1) {
					// ==========================================================
					out.println("*** Already downloading another file of the "
							+ this.botName);
					Thread.sleep(60000);
					// ==========================================================
				} else if (message.indexOf("All Slots Full") != -1) {
					// ==========================================================
					out.println("*** ");
					inMainQueue = true;
					// ==========================================================
				} else if (message.equals("connection lost")) { //
					// ==========================================================
					this.prepareConnection();
					this.writer.register(this.nickName, this.location,
							this.fullName, this.eMail, this.password);
					loggedIn = false;
					channelJoined = false; //
					// ==========================================================
				} else if( message.equals("resume current download")) {
					out.println("*** Resuming canceled download " + downloadPackage.getFileName());
					this.prepareConnection();
					this.writer.register(this.nickName, this.location,
							this.fullName, this.eMail, this.password);
					loggedIn = false;
					channelJoined = false;
				}

			} while (stayActive);
		} catch (InterruptedException ie) {
			if (download != null) {
				download.interrupt();
			}
			this.reader.close();
			this.writer.close();
			System.out.println("IRCThread abgebrochen");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DCCPackage evaluateDownloadMessage(String message) {
		int index = ((String) message).indexOf("DCC SEND");
		String line = ((String) message).substring(index + "DCC SEND".length()
				+ 1);
		String[] splits = line.split("\\s+");
		DCCPackage dccPackage = new DCCPackage();
		dccPackage.setFileName(splits[0]);
		dccPackage.setIP(Long.valueOf(splits[1].replaceAll("[^0-9]+", "")));
		dccPackage
				.setPort(Integer.valueOf(splits[2].replaceAll("[^0-9]+", "")));
		dccPackage.setFileSize(Long
				.valueOf(splits[3].replaceAll("[^0-9]+", "")));
		return dccPackage;
	}

	@Override
	public void run() {
		try {
			this.prepareConnection();
			this.download();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// ork: irc.abjects.org -- Channel: evil
		//  [xdcc]|bka-03	# of Packs: 24 
		IRCThread irc = new IRCThread();
		irc.setIrcServer("irc.abjects.org");
		irc.setIrcChannel("evil");
		irc.setBotName("[xdcc]|bka-03");
		irc.setPackageNr("24");
		Thread a = new Thread(irc);
		a.start();
		// Thread.sleep(60000);
		// a.interrupt();
	}

}
