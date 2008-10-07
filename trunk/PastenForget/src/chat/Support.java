package chat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import parser.Parser;

public class Support {
	private Message message = null;
	private final URL server;

	public Support(URL server) {
		this.server = server;
	}

	public boolean sendMessage() throws IOException {
		if (message != null) {
			String link = this.server.toString() + "?" + "name="
					+ URLEncoder.encode(message.getName(), "UTF-8") + "&date="
					+ URLEncoder.encode(message.getDate(), "UTF-8")
					+ "&question="
					+ URLEncoder.encode(message.getQuestion(), "UTF-8");
			this.message = null;
			URL url = new URL(link);
			InputStream is = url.openConnection().getInputStream();
			String page = Parser.convertStreamToString(is, true);
			if(Parser.getComplexTag("success", page).size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public List<Message> receiveMessages() throws IOException {
		List<Message> messages = new ArrayList<Message>();
		URL url = this.server;
		InputStream is = url.openConnection().getInputStream();
		String page = Parser.convertStreamToString(is, true);
		for(String msg : Parser.getComplexTag("message", page)) {
			String nameTag = Parser.getComplexTag("name", msg).get(0);
			String dateTag = Parser.getComplexTag("date", msg).get(0);
			String questionTag = Parser.getComplexTag("question", msg).get(0);
			String name = Parser.getTagContent("name", nameTag);
			String date = Parser.getTagContent("date", dateTag);
			String question = Parser.getTagContent("question", questionTag);
			if(Parser.getComplexTag("answer", msg).size() == 0) {
				messages.add(new Message(name, date, question));
			} else {
				String answerTag = Parser.getComplexTag("answer", msg).get(0);
				String answer = Parser.getTagContent("answer", answerTag);
				messages.add(new Message(name, date, question, answer));
			}
		}
		return messages;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
