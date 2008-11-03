package download.irc;

import java.util.regex.Pattern;

public class Messages {
	public final String server;
	public final String channel;
	public final String bot;
	public final String pack;
	public final String nickName;
	
	public Messages(String server, String channel, String bot, String pack, String nickName) {
		this.server = server;
		this.channel = channel;
		this.bot = bot;
		this.pack = pack;
		this.nickName = nickName;
		this.compilePattern();
		
	}
	
	private void compilePattern() {
		DCC_SEND_DOWNLOAD = Pattern.compile(":" + this.bot.replaceAll("[^\\w]+", ".+") + ".*[^X]{1}DCC SEND.*\\s+[0-9]+.*\\s+[0-9]+.*\\s+[0-9]+.*", Pattern.CASE_INSENSITIVE);
		ADDED_TO_MAIN_QUEUE = Pattern.compile(":" + this.bot.replaceAll("[^\\w]+", ".+") + ".*All Slots Full, Added you to the main queue in position [0-9]+.*", Pattern.CASE_INSENSITIVE);
		MAIN_QUEUE_FULL = Pattern.compile(":" + this.bot.replaceAll("[^\\w]+", ".+") + ".*All Slots Full, Denied.*", Pattern.CASE_INSENSITIVE);
		MESSAGE_FOR_ME = Pattern.compile(".*" + this.nickName + ".*", Pattern.CASE_INSENSITIVE);
	}
	
	public Pattern DCC_SEND_DOWNLOAD;
	
	public Pattern ADDED_TO_MAIN_QUEUE;
	
	public Pattern MAIN_QUEUE_FULL;
	
	public Pattern MESSAGE_FOR_ME;
	
	public final Pattern PACKAGE_ALREADY_REQUESTED = Pattern.compile(".*You already requested that pack.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern END_PACKAGE_ALREADY_REQUESTED = Pattern.compile(".*Closing Connection: DCC Timeout.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern ALREADY_DOWNLOADING_OTHER_FILE = Pattern.compile(".*You can only have.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern DOWNLOAD_LIMIT_EXCEEDED = Pattern.compile(".*Sorry, I have exceeded my transfer.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern CONNECTED_TO_SERVER = Pattern.compile(".*\\s+001\\s+.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern READY_TO_CONNECT_TO_CHANNEL = Pattern.compile(".*\\s+376\\s+.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern CONNECTED_TO_CHANNEL = Pattern.compile(".*\\s+332\\s+.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern XDCC_SEND_DENIED = Pattern.compile(".*XDCC SEND denied.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern READER_CLOSED = Pattern.compile("connection lost", Pattern.CASE_INSENSITIVE);
	
	public final Pattern FILE_TRANSFER_NOT_FINISHED = Pattern.compile("resume current download", Pattern.CASE_INSENSITIVE);
	
	public final Pattern MESSAGE_WITH_UNHANDLED_IDENT_CODE = Pattern.compile(".*\\s+[0-9]{3}\\s+.*", Pattern.CASE_INSENSITIVE);

	public final Pattern SERVER_FULL = Pattern.compile(".*Error.*the server is full.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern TOO_MANY_CONNECTIONS = Pattern.compile(".*throttled for .* for too many connections.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern NO_MOTD_FILE = Pattern.compile(".*\\s+422\\s+.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern ERROR = Pattern.compile(".*Error.*", Pattern.CASE_INSENSITIVE);
	
	public final Pattern CANNOT_JOIN_CHANNEL = Pattern.compile(".*\\s+474\\s+.*");
	/*
	 * - indexOf(input) != -1    <==> .* + input + .*
	 * - equals(input) == true   <==> input without \\s\\w+.*
	 * - matches(input) == true  <==> input with \\s\\w+*.
	 */

}
