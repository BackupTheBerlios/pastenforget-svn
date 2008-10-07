package chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import parser.Parser;

public class PnF_Chat {
	public static void write(String sender) throws Exception {
		System.out.println("Bitte geben Sie den Empfängernamen ein");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String empfaenger = br.readLine();
		System.out.println("Bitte geben Sie eine Nachricht ein!");
		br = new BufferedReader(new InputStreamReader(System.in));
		String message = br.readLine();
		String link = "http://www.handballwoelfe.de/server.php?"
				+ "dir=send&" + "sender=" + URLEncoder.encode(sender, "UTF-8")
				+ "&empfaenger=" + URLEncoder.encode(empfaenger, "UTF-8")
				+ "&msg=" + URLEncoder.encode(message, "UTF-8");
		URL url = new URL(link);
		InputStream in = url.openConnection().getInputStream();
		br = new BufferedReader(new InputStreamReader(in));
		String page = Parser.convertStreamToString(in, false);
		if(Parser.getComplexTag("success", page).size() > 0) {
			System.out.println("Nachricht versendet!\n");
		}
	}

	public static void read(String empfaenger) throws Exception {
		System.out.println("empfangene Nachrichten");
		String link = "http://www.handballwoelfe.de/server.php?"
				+ "dir=rcv&empfaenger=" + URLEncoder.encode(empfaenger, "UTF-8");
		URL url = new URL(link);
		InputStream in = url.openConnection().getInputStream();
		String page = Parser.convertStreamToString(in, false);
		List<String> messages = Parser.getComplexTag("message", page);
		for(String message : messages) {
			String sender = Parser.getAttribute("sender", message);
			String text = Parser.getTagContent("message", message);
			System.out.println(sender  + ": " + text + "\n");
		}
	}

	
	public static void controller(String name) throws Exception {
		System.out.println("(1) Nachricht schreiben");
		System.out.println("(2) Nachrichten empfangen");
		System.out.println("(3) beenden");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int choice = Integer.valueOf(br.readLine()).intValue();

		switch (choice) {
		case 1:
			write(name);
			break;
		case 2:
			read(name);
			break;
		case 3:
			System.exit(0);
		}
		controller(name);
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println("Paste'n'Forget Support Chat");
		System.out.println("Bitte geben Sie Ihren Namen ein!");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String name = br.readLine();
		controller(name);

		
	}
}
