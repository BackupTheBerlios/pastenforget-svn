package irc;
import java.util.Stack;


public class DCCPackage {

	private String fileName;
	private String ip;
	private Integer port;
	private Long fileSize;
	private Long downloadedFileSize;

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setIP(Long ip) {
		this.ip = this.LongToIP(ip);
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	public void setDownloadedFileSize(Long downloadedFileSize) {
		this.downloadedFileSize = downloadedFileSize;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getIP() {
		return this.ip;
	}

	public Integer getPort() {
		return this.port;
	}

	public Long getFileSize() {
		return this.fileSize;
	}
	
	public Long getDownloadedFileSize() {
		return this.downloadedFileSize;
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
			ipBuffer
					.append((ipParts.peek() == ipParts.firstElement()) ? ipParts
							.pop()
							: ipParts.pop() + ".");
		}
		return ipBuffer.toString();
	}
	
	@Override
	public String toString() {
		return new String("filename = " + this.getFileName() +  "; ip = " + this.getIP() + "; port = " + this.getPort() + "; filesize = " + this.getFileSize());
	}

}