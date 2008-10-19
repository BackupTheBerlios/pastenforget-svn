package decrypt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RSDF {

	// Construct data

	public static List<String> decodeRSDF(File rsdfFile) {
		try {
			String hostname = "cschaedl.spacequadrat.de";
			int port = 80;
			InetAddress addr = InetAddress.getByName(hostname);
			Socket socket = new Socket(addr, port);

			// Send header
			String path = "/rsdf_decrypt.php";

			// File To Upload
			File theFile = rsdfFile;

			System.out.println("size: " + (int) theFile.length());
			DataInputStream fis = new DataInputStream(new BufferedInputStream(
					new FileInputStream(theFile)));
			byte[] theData = new byte[(int) theFile.length()];

			fis.readFully(theData);
			fis.close();

			DataOutputStream raw = new DataOutputStream(socket
					.getOutputStream());
			Writer wr = new OutputStreamWriter(raw);

			String command = "--dill\r\n"
					+ "Content-Disposition: form-data; name=\"rsdffile\"; filename=\""
					+ theFile.getName() + "\"\r\n"
					+ "Content-Type: multipart/form-data\r\n" + "\r\n";

			String trail = "\r\n--dill--\r\n";

			String header = "POST "
					+ path
					+ " HTTP/1.0\r\n"
					+ "Accept: */*\r\n"
					+ "Referer: http://localhost\r\n"
					+ "Accept-Language: de\r\n"
					+ "Content-Type: multipart/form-data; boundary=dill\r\n"
					+ "User_Agent: TESTAGENT\r\n"
					+ "Host: "
					+ hostname
					+ "\r\n"
					+ "Content-Length: "
					+ ((int) theFile.length() + command.length() + trail
							.length()) + "\r\n" + "Connection: Keep-Alive\r\n"
					+ "Pragma: no-cache\r\n" + "\r\n";

			wr.write(header);
			wr.write(command);

			wr.flush();
			raw.write(theData);
			raw.flush();
			wr.write("\r\n--dill--\r\n");
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			String line;
			List<String> links = new ArrayList<String>();
			while ((line = rd.readLine()) != null) {
				links.add(line);
			}
			wr.close();
			raw.close();

			socket.close();

			return links;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
