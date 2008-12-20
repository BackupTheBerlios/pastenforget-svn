package download.irc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import queue.Queue;
import stream.SpeedThread;
import download.Download;
import download.Status;
import exception.CancelException;
import exception.RestartException;
import exception.StopException;
import filtration.RequestPackage;

public class IRC extends Download implements Runnable {
	private final Map<String, String> logs = new HashMap<String, String>();
	private PrintStream out = System.out;
	private String message = new String();
	private boolean inMainQueue = false;

	/*
	 * Just for error code handling
	 */

	PrintWriter pWriter = null;

	/*
	 * Information needed and provided by IRC
	 */
	private String ircServer;
	private String fullName = "guest guestersen";
	private String nickName = "guest" + String.valueOf(System.currentTimeMillis());
	private String password = "abcde";
	private String eMail = "nick.name@muster.ru";
	private String location = "At-home";
	private String ircChannel;
	private String botName;
	private String packageNr;
	private Integer ircPort = new Integer(6667);
	private Integer downloadPort;
	private String downloadIp;
	private Long downloadedFileSize;
	

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

	public IRC() {
		super();
		/*
		 * Just for error code handling
		 */

		try {
			this.pWriter = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream("code.log", true), "UTF-8"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
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

	public void setIrcPort(Integer ircPort) {
		this.ircPort = ircPort;
	}
	
	public void setDownloadIp(Long ip) {
		this.downloadIp = this.LongToIP(ip);
	}
	
	public String getDownloadIp() {
		return this.downloadIp;
	}
	
	public void setDownloadPort(Integer port) {
		this.downloadPort = port;
	}
	
	public Integer getDownloadPort() {
		return this.downloadPort;
	}
	
	public void setDownloadedFileSize(Long downloadedFileSize) {
		this.downloadedFileSize = downloadedFileSize;
	}
	
	public Long getDownloadedFileSize() {
		return this.downloadedFileSize;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	@Override
	public synchronized boolean stop() {
		if (thread != null)
			thread = null;
		this.setCurrentSize(0);
		this.setStatus(Status.getStopped());
		this.setStopped(true);
		this.setStarted(false);
		try {
			if((this.eventQueue != null) && (thread.isAlive()))
				this.eventQueue.put("stopped");
		} catch (Exception e) {
			
		}
		return true;
	}

	@Override
	public synchronized boolean cancel() {
		if (thread != null)
			thread = null;
		this.setStatus(Status.getCanceled());
		this.setCanceled(true);
		this.setStarted(false);
		if (this.isStopped()) {
			String filename = new String();
			if (this.getDestination() == null) {
				filename = this.getFileName();
			} else {
				filename = this.getDestination().getPath() + File.separator
						+ this.getFileName();
			}
			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}
			System.out.println("Download canceled: " + this.getFileName());
		}
		try {
			if((this.eventQueue != null) && (thread.isAlive()))
				this.eventQueue.put("canceled");
		} catch (Exception e) {
			
		}
		return true;
	}
	
	/**
	 * Überprüft, ob (von außen) die Funktionen cancel() oder stop() ausgeführt
	 * wurden und sollte dies der Fall sein, wird entweder eine StopException
	 * oder eine CancelException geworfen.
	 * 
	 * @throws StopException
	 * @throws CancelException
	 * 
	 * private void checkStatus() throws StopException, CancelException { if
	 * (this.isCanceled()) { throw new CancelException(); } if
	 * (this.isStopped()) { throw new StopException(); } }
	 */
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

	protected void onServerIsFull() throws RestartException {
		this.out.println("*** Server is full - retry in 60 Seconds");
		this.setStatus(Status.getError("server full"));
		this.sleepSek(60);
		throw new RestartException();
	}

	protected void onNickNameInUse() throws RestartException {
		this.createNewNickname();
		throw new RestartException();
	}
	
	protected void onConnectedToServer() {
		this.out.println("*** Connected to Server " + this.ircServer);
	}

	protected void onTooManyConnections() throws RestartException {
		this.out.println("*** Too many connections");
		this.setStatus(Status.getError("too many connections"));
		this.sleepSek(60);
		throw new RestartException();
	}

	protected void onConnectionProcessFinished() {
		this.out.println("*** Connecting to Channel " + this.ircChannel);
		this.writer.joinChannel(this.ircChannel);
	}

	protected void onConnectedToChannel() {
		this.out.println("*** Connected to Channel " + this.ircChannel);
		if (!this.inMainQueue) {
			this.writer.sendCTCP(botName, "xdcc send " + this.packageNr);
		}
	}

	protected void onBotSendingFile() {
		inMainQueue = false;
		this.out.println("*** Bot " + this.botName + " sends Download Package ");
		this.evaluateDownloadMessage(message);
		this.out.println("*** Sending Accept to " + this.botName + " for Send");
		this.writer.sendCTCP(this.botName, "dcc accept "
				+ this.getFileName() + " "
				+ this.getDownloadPort() + " 0");
		File file = new File(this.getDestination() + "/" + this.getFileName());
		Long downloadedFileSize = new Long(0);
		if (file.exists()) {
			downloadedFileSize = file.length();
		}
		this.setCurrentSize(downloadedFileSize);
		this.setDownloadedFileSize(downloadedFileSize);
		this.sleepSek(3);
		this.out.println("*** Sending Resume [" + this.getFileName()
				+ " " + this.getDownloadPort() + " "
				+ this.getDownloadedFileSize() + "] to"
				+ this.botName);
		this.writer.sendCTCP(this.botName, "dcc resume "
				+ this.getFileName() + " "
				+ this.getDownloadPort() + " "
				+ this.getDownloadedFileSize());
		this.sleepSek(3);
		this.out.println("*** Sending Accept to " + this.botName
				+ " for Resume");
		this.writer.sendCTCP(this.botName, "dcc accept "
				+ this.getFileName() + " "
				+ this.getDownloadPort() + " "
				+ this.getDownloadedFileSize());
	}

	protected void onTransferDenied() throws StopException {
		this.out.println("*** Transfer denied");
		this.setStatus(Status.getError("transfer denied"));
		throw new StopException();
	}

	protected void onPackageAlreadyRequested() throws StopException, CancelException {
		this.out.println("*** Package already requested");
		this.wait(180);
		this.onConnectedToChannel();
	}

	protected void onPackageNotRequested() {
		// ignore this.writer.sendCTCP(botName, "xdcc send #" + this.packageNr);
	}

	protected void onOnlyOneDownloadAllowed() throws StopException, CancelException {
		this.out.println("*** Already downloading another file of the " + this.botName);
		this.wait(180);
		this.onConnectedToChannel();
	}

	protected void onMainQueueFull() throws StopException {
		this.out.println("*** Main Queue is full");
		this.setStatus(Status.getError("main queue full"));
		throw new StopException();
		
	}

	protected void onAddedToMainQueue() {
		this.setStatus(Status.getError("Added To Main Queue"));
		this.out.println("*** Add to Main Queue");
		inMainQueue = true;
	}

	protected void onDownloadInterrupted() {
	}

	protected void onReaderClosed() {
	}

	protected void onErrorCodeMessage() {
		/*
		 * Just for error code handling
		 */
		String regex = "\\s+[0-9]{3}\\s+";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(message);
		if (m.find()) {
			String code = m.group().replaceAll("\\s+", "");
			if (this.logs.get(code) == null) {
				this.logs.put(code, message
						.substring(message.indexOf(code) + 3));
				this.out.println(code + ":"
						+ message.substring(message.indexOf(code) + 3));
				pWriter.println(code + ":"
						+ message.substring(message.indexOf(code) + 3));
				pWriter.flush();
			}

		}
	}

	protected void onCannotJoinChannel() throws StopException, CancelException {
		this.setStatus(Status.getError("Added To Main Queue"));
		throw new StopException();
	}

	@Override
	public URLConnection prepareConnection() throws StopException,CancelException, RestartException, IOException {
		this.eventQueue = new SynchronousQueue<String>();
		this.out.println("*** Connecting to " + this.ircServer);
		try {
			int timeout = 1000;
			this.socket = new Socket();
			this.socket.bind(null);
			this.socket.connect(new InetSocketAddress(this.ircServer, this.ircPort), timeout); 
			this.reader = new Reader(this.socket.getInputStream(), this.eventQueue);
			this.writer = new Writer(this.socket.getOutputStream());
		} catch (Exception e) {
			this.setStatus(Status.getError("no connection"));
			this.sleepSek(60);
			throw new StopException();
		}
		this.reader.start();
		this.writer.register(this.nickName, this.location, this.fullName, this.eMail, this.password);
		Messages messages = new Messages(this.ircServer, this.ircChannel, this.botName, this.packageNr, this.nickName);
		do {
			try {
				this.message = this.eventQueue.take();
			} catch(InterruptedException ie) {
				ie.printStackTrace();
			}
			this.checkStatus();
			if (messages.MESSAGE_FOR_ME.matcher(this.message).matches()) {
				this.out.println("*** MESSAGE: " + this.message);
			}
			if (messages.CONNECTED_TO_SERVER.matcher(this.message).matches()) {
				this.onConnectedToServer();
			} else if (messages.SERVER_FULL.matcher(this.message).matches()) {
				this.onServerIsFull();
			} else if (messages.NICKNAME_IN_USE.matcher(this.message).matches()) { 
				this.onNickNameInUse();
			} else if (messages.TOO_MANY_CONNECTIONS.matcher(this.message).matches()) {
				this.onTooManyConnections();
			} else if (messages.READY_TO_CONNECT_TO_CHANNEL.matcher(this.message).matches() || messages.NO_MOTD_FILE.matcher(this.message).matches()) {
				this.onConnectionProcessFinished();
			} else if (messages.CANNOT_JOIN_CHANNEL.matcher(this.message).matches()) {
				this.onCannotJoinChannel();
			} else if (messages.CONNECTED_TO_CHANNEL.matcher(this.message).matches()) {
				this.onConnectedToChannel();
			} else if (messages.DCC_SEND_DOWNLOAD.matcher(this.message).matches()) {
				this.onBotSendingFile();
				break;
			} else if (messages.XDCC_SEND_DENIED.matcher(this.message).matches()) {
				this.onTransferDenied();
			} else if (messages.PACKAGE_ALREADY_REQUESTED.matcher(this.message).matches()) {
				this.onPackageAlreadyRequested();
			} else if (messages.END_PACKAGE_ALREADY_REQUESTED.matcher(this.message).matches()) {
				this.onPackageNotRequested();
			} else if (messages.ALREADY_DOWNLOADING_OTHER_FILE.matcher(message).matches()) {
				this.onOnlyOneDownloadAllowed();
			} else if (messages.ADDED_TO_MAIN_QUEUE.matcher(this.message).matches()) {
				this.onAddedToMainQueue();
			} else if (messages.MAIN_QUEUE_FULL.matcher(this.message).matches()) {
				this.onMainQueueFull();
			} else if (messages.READER_CLOSED.matcher(this.message).matches()) {
				this.onReaderClosed();
			} else if (messages.DOWNLOAD_CANCELED.matcher(this.message).matches()) {
				this.reader.close();
				this.sleepSek(100);
				throw new CancelException(); 
			} else if (messages.DOWNLOAD_STOPPED.matcher(this.message).matches()) { 
				this.reader.close();
				this.sleepSek(100);
				throw new StopException();
			} else if (messages.FILE_TRANSFER_NOT_FINISHED.matcher(this.message).matches()) {
				this.onDownloadInterrupted();
			} else if (messages.MESSAGE_WITH_UNHANDLED_IDENT_CODE.matcher(this.message).matches()) {
				this.onErrorCodeMessage();
			}
		} while (true);
		System.out.println("Controller closed");
		return null;
	}

	public void download(BufferedInputStream is, RandomAccessFile raf) throws StopException, CancelException, RestartException, IOException {
		new Thread(new SpeedThread(this)).start();
		this.setStatus(Status.getActive());
		Socket socket = null;

		String filename = this.getFileName();
		String ip = this.getDownloadIp();
		Integer port = this.getDownloadPort();
		Long filesize = this.getFileSize();
		
		System.out.println("Filename: " + filename);
		System.out.println("IP: " + ip);
		System.out.println("Port: " + port);
		System.out.println("Filesize: " + filesize + " Bytes");

		File file = new File(this.getDestination() + "/" + filename);
		raf = new RandomAccessFile(file, "rw");
		raf.seek(this.getDownloadedFileSize());
		socket = new Socket(ip, port);
		is = new BufferedInputStream(socket.getInputStream());
		byte[] buffer = new byte[1024];	
		int receivedBytes;
		while ((receivedBytes = is.read(buffer)) > -1) {
			this.checkStatus();
			this.setCurrentSize(this.getCurrentSize() + receivedBytes);
			raf.write(buffer, 0, receivedBytes);
		}
		if (this.getCurrentSize() < this.getFileSize()) {
			throw new RestartException();
		}
	}
	
	
	public void evaluateDownloadMessage(String message) {
		int index = ((String) message).indexOf("DCC SEND");
		String line = ((String) message).substring(index + "DCC SEND".length()
				+ 1);
		String[] splits = line.split("\\s+");
		DCCPackage dccPackage = new DCCPackage();
		dccPackage.setFileName(splits[0]);
		
		this.setFileName(splits[0]);
		this.setDownloadIp(Long.valueOf(splits[1].replaceAll("[^0-9]+", "")));
		this.setDownloadPort(Integer.valueOf(splits[2].replaceAll("[^0-9]+", "")));
		this.setFileSize(Long.valueOf(splits[3].replaceAll("[^0-9]+", "")));
	}
	
	private String LongToIP(Long ip) {
		Long current = ip;
		Stack<String> ipParts = new Stack<String>();
		StringBuffer ipBuffer = new StringBuffer();
		do {
			ipParts.push(String.valueOf((current % 256)));
			current /= 256;
		} while (ipParts.size() < 4);

		while (!ipParts.isEmpty()) {
			ipBuffer.append((ipParts.peek() == ipParts.firstElement()) ? ipParts.pop() : ipParts.pop() + ".");
		}
		return ipBuffer.toString();
	}
	
	private void createNewNickname() {
		this.nickName = "guest" + String.valueOf(System.currentTimeMillis());
	}
	
	public void closeConnection(BufferedInputStream is, RandomAccessFile raf) {
		try {
			if(this.reader != null) {
				this.reader.close();
			}
			if(this.socket != null) {
				this.socket.close();
			}
			if(is != null) {
				is.close();
			}
			if(raf != null) {
				raf.close();
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		RandomAccessFile raf = null;
		BufferedInputStream is = null;
		try {
			this.readLog();
			this.prepareConnection();
			this.checkStatus();
			this.download(is, raf);
			this.setStatus(Status.getFinished());
			System.out.println("Download finished: " + this.getFileName());
			this.getQueue().downloadFinished(this);	
		} catch (StopException stop) {
			this.closeConnection(is, raf);
			System.out.println("Download stopped: " + this.getFileName());
		} catch (CancelException cancel) {
			this.closeConnection(is, raf);
			this.getQueue().downloadFinished(this);	
			System.out.println("Download canceled: " + this.getFileName());
		} catch (IOException io) {
			this.closeConnection(is, raf);
			io.printStackTrace();
		} catch (RestartException re) {
			this.closeConnection(is, raf);
			System.out.println("Download restarted: " + this.ircServer + " / " + this.ircChannel);
			this.run();
		}
	}
}
