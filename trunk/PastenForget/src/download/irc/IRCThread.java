package download.irc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import queue.Queue;
import download.Download;
import download.Status;
import exception.CancelException;
import exception.StopException;
import filtration.RequestPackage;

public class IRCThread extends Download implements Runnable {
	private final Map<String, String> logs = new HashMap<String, String>();
	private PrintStream out = System.out;
	
	/*
	 * Information needed and provided by IRC
	 */
	private String ircServer;
	private String fullName = "guest guestersen";
	private String nickName = "guest" + String.valueOf(System.currentTimeMillis()).substring(5);
	private String password = "abcde";
	private String eMail = "nick.name@muster.ru";
	private String location = "At-home";
	private String ircChannel;
	private String botName;
	private String packageNr;
	private Integer port = new Integer(6667);

	/*
	 * Objects needed to communicate with IRC Server
	 */
	private Reader reader;
	private Writer writer;
	private Socket socket;

	/*
	 * Communication between Reader and this (Controller)
	 */
	private BlockingQueue<String> eventQueue = new SynchronousQueue<String>();;

	public IRCThread() {
		super();
	}

	@Override
	public void setInformation(URL url, File destination, Queue queue) {
		RequestPackage ircPackage = this.getIrc();
		this.setIrcServer(ircPackage.getIrcServer());
		this.setIrcChannel(ircPackage.getIrcChannel());
		this.setBotName(ircPackage.getBotName());
		this.setPackageNr(ircPackage.getPackage());
		this.setStatus(Status.getWaiting());
		this.setFileName(ircPackage.getDescription());
		this.setDestination(destination);
		this.setQueue(queue);
		this.setStatus(Status.getWaiting());
		this.setCurrentSize(0);
		try {
			this.setUrl(new URL("http://irc." + ircPackage.getIrcServer()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

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

	/**
	 * Refreshs a existing connection or builds a new connection
	 *
	 * @throws IOException
	 */
	
	private void prepareConnection() {
		/*
		 * Anonymitätsfeature 
		 * - System.setProperty("proxyPort","4001");
		 * - System.setProperty("proxyHost","127.0.0.1");
		 */
		try {
			this.eventQueue = new SynchronousQueue<String>();
			this.out.println("*** Connecting to " + this.ircServer);
			this.socket = new Socket(this.ircServer, this.port);
			this.reader = new Reader(this.socket.getInputStream(), this.eventQueue);
			this.writer = new Writer(this.socket.getOutputStream());
			new Thread(this.reader).start();
		} catch (IOException io) {
			if (io.equals(new ConnectException())) {
				System.out.println("ConnectException");
			}
			System.out.println("Connection failure");
			this.sleepSek(60);
			this.prepareConnection();
		}
	}

	/**
	 * Überprüft, ob (von außen) die Funktionen cancel() oder stop() ausgeführt
	 * wurden und sollte dies der Fall sein, wird entweder eine StopException
	 * oder eine CancelException geworfen.
	 * 
	 * @throws StopException
	 * @throws CancelException
	 */
	private void checkStatus() throws StopException, CancelException {
		if (this.isCanceled()) {
			throw new CancelException();
		}
		if (this.isStopped()) {
			throw new StopException();
		}
	}

	/**
	 * Freezes the thread for specified seconds
	 * 
	 * @param sek
	 */
	private void sleepSek(double sek) {
		try {
			long millis = (long) (sek * 1000);
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the file code.log. Important for error code handling.
	 */
	private void readLog() {
		try {
			InputStream is = new FileInputStream("code.log");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				String regex = ":";
				Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(line);
				m.find();
				String code = line.substring(0, m.start());
				String message = line.substring(m.start() + 1);
				this.logs.put(code, message);
			}
		} catch (Exception e) {
		}
	}

	public void download() {
		DCCDownload download = null;
		try {
			/*
			 * Just for error code handling
			 */
			OutputStream os = new FileOutputStream("code.log", true);
			PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));

			checkStatus();
			this.writer.register(this.nickName, this.location, this.fullName,
					this.eMail, this.password);
			boolean inMainQueue = false;
			boolean stayActive = true;
			DCCPackage downloadPackage = null;
			String message = new String();
			Messages messages = new Messages(this.ircServer, this.ircChannel,
					this.botName, this.packageNr, this.nickName);

			do {
				checkStatus();
				message = this.eventQueue.take();
				
				if(messages.MESSAGE_FOR_ME.matcher(message).matches()) {
					this.out.println("*** MESSAGE: " + message);
				}
				
				if (messages.CONNECTED_TO_SERVER.matcher(message).matches()) {
					this.out.println("*** Connected to Server "
							+ this.ircServer);
				} else if(messages.SERVER_FULL.matcher(message).matches()) { 
					this.out.println("*** Server is full - retry in 60 Seconds");
					this.sleepSek(60);
				} else if(messages.TOO_MANY_CONNECTIONS.matcher(message).matches()) { 
					this.out.println("*** Too many connections");
				} else if (messages.READY_TO_CONNECT_TO_CHANNEL.matcher(message).matches()
						|| messages.NO_MOTD_FILE.matcher(message).matches()) {
					this.out.println("*** Connecting to Channel "
							+ this.ircChannel);
					this.writer.joinChannel(this.ircChannel);
				} else if (messages.CONNECTED_TO_CHANNEL.matcher(message).matches()) {
					this.out.println("*** Connected to Channel "
							+ this.ircChannel);
					if (!inMainQueue) {
						this.writer.sendCTCP(botName, "xdcc send "
								+ this.packageNr);
					}
				} else if (messages.DCC_SEND_DOWNLOAD.matcher(message).matches()) {
					inMainQueue = false;
					this.out.println("*** Bot " + this.botName
							+ " sends Download Package ");
					downloadPackage = evaluateDownloadMessage(message);
					this.out.println("*** Sending Accept to " + this.botName
							+ " for Send");
					this.writer.sendCTCP(this.botName, "dcc accept "
							+ downloadPackage.getFileName() + " "
							+ downloadPackage.getPort() + " 0");
					String fileName = downloadPackage.getFileName();
					this.setFileName(fileName);
					File file = new File(fileName);
					Long downloadedFileSize = new Long(0);
					if (file.exists()) {
						downloadedFileSize = file.length();
					}
					this.setCurrentSize(downloadedFileSize);
					this.setFileSize(downloadPackage.getFileSize());
					downloadPackage.setDownloadedFileSize(downloadedFileSize);
					this.sleepSek(3);
					this.out.println("*** Sending Resume ["
							+ downloadPackage.getFileName() + " "
							+ downloadPackage.getPort() + " "
							+ downloadPackage.getDownloadedFileSize() + "] to"
							+ this.botName);
					this.writer.sendCTCP(this.botName, "dcc resume "
							+ downloadPackage.getFileName() + " "
							+ downloadPackage.getPort() + " "
							+ downloadPackage.getDownloadedFileSize());
					this.sleepSek(3);
					this.out.println("*** Sending Accept to " + this.botName
							+ " for Resume");
					this.writer.sendCTCP(this.botName, "dcc accept "
							+ downloadPackage.getFileName() + " "
							+ downloadPackage.getPort() + " "
							+ downloadPackage.getDownloadedFileSize());
					download = new DCCDownload(downloadPackage,
							this.eventQueue, this);
					Thread dl = new Thread(download);
					dl.start();
				} else if (messages.XDCC_SEND_DENIED.matcher(message).matches()) {
					this.out.println("*** Transfer denied");
					stayActive = false;
				} else if (messages.PACKAGE_ALREADY_REQUESTED.matcher(message).matches()) {
					this.out.println("*** Package already requested");
					this.writer.sendCTCP(botName, "dcc cancel");
				} else if (messages.END_PACKAGE_ALREADY_REQUESTED.matcher(
						message).matches()) {
					this.writer.sendCTCP(botName, "xdcc send #"
							+ this.packageNr);
				} else if (message.indexOf("You can only have") != -1) {
					this.out
							.println("*** Already downloading another file of the "
									+ this.botName);
					sleepSek(60);
				} else if (messages.ADDED_TO_MAIN_QUEUE.matcher(message)
						.matches()) {
					this.out.println("*** Add to Main Queue");
					inMainQueue = true;
				} else if (messages.MAIN_QUEUE_FULL.matcher(message).matches()) {
					this.out.println("*** Main Queue is full");
				} else if (messages.READER_CLOSED.matcher(message).matches()) {
					this.prepareConnection();
					this.writer.register(this.nickName, this.location,
							this.fullName, this.eMail, this.password);
				} else if (messages.FILE_TRANSFER_NOT_FINISHED.matcher(message).matches()) {
					this.out.println("*** Resuming canceled download "
							+ downloadPackage.getFileName());
					this.prepareConnection();
					this.writer.register(this.nickName, this.location,
							this.fullName, this.eMail, this.password);
				} else if (messages.MESSAGE_WITH_UNHANDLED_IDENT_CODE.matcher(message).matches()) {
					/*
					 * Just for error code handling
					 */
					String regex = "\\s+[0-9]{3}\\s+";
					Pattern p = Pattern
							.compile(regex, Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher(message);
					if (m.find()) {
						String code = m.group().replaceAll("\\s+", "");
						if (this.logs.get(code) == null) {
							this.logs.put(code, message.substring(message
									.indexOf(code) + 3));
							this.out.println(code + ":" + message.substring(message.indexOf(code) + 3));
							pWriter.println(code + ":" + message.substring(message.indexOf(code) + 3));
							pWriter.flush();
						}

					}
				}

			} while (stayActive);
		} catch (InterruptedException ie) {
		} catch (CancelException ce) {
			this.out.println("Download canceled: [" + this.ircServer + ", "
					+ this.ircChannel + ", " + this.botName + ", "
					+ this.packageNr + "]");
		} catch (StopException se) {
			this.out.println("Download stopped: [" + this.ircServer + ", "
					+ this.ircChannel + ", " + this.botName + ", "
					+ this.packageNr + "]");
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
		this.setStatus(Status.getStarted());
		this.readLog();
		this.prepareConnection();
		this.download();

	}
}
