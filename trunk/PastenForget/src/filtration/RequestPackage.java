package filtration;

public class RequestPackage {
	private final String active;
	private final String slots;
	private final String queue;
	private final String speed;
	private final String downloaded;
	private final String ircServer;
	private final String ircChannel;
	private final String botName;
	private final String packageNumber;
	private final String fileSize;
	private final String fileName;
	
	
	public RequestPackage(String active, String slots, String queue,
			String speed, String downloaded, String ircServer, String ircChannel, String botName,
			String packageNumber, String fileSize, String fileName) {
		this.active = active;
		this.slots = slots;
		this.queue = queue;
		this.speed = speed;
		this.downloaded = downloaded;
		this.ircServer = ircServer;
		this.ircChannel = ircChannel;
		this.botName = botName;
		this.packageNumber = packageNumber;
		this.fileSize = fileSize;
		this.fileName = fileName;
	}
	
	public String getActive() {
		return this.active;
	}
	
	public String getSlots() {
		return this.slots;
	}
	
	public String getQueue() {
		return this.queue;
	}
	
	public String getSpeed() {
		return this.speed;
	}
	
	public String getDownloaded() {
		return this.downloaded;
	}
	
	public String getIrcServer() {
		return this.ircServer;
	}
	
	public String getIrcChannel() {
		return this.ircChannel;
	}
	
	public String getBotName() {
		return this.botName;
	}
	
	public String getPackage() {
		return this.packageNumber;
	}
	
	public String getFileSize() {
		return this.fileSize;
	}
	
	public String getFileName() {
		return this.fileName;
	}
}
