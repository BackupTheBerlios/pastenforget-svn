package chat;

public class Message {
	private final String name;
	private final String date;
	private final String question;
	private final String answer;

	public Message(String name, String date, String question) {
		this.name = name;
		this.date = date;
		this.question = question;
		this.answer = null;
	}
	
	public Message(String name, String date, String question, String answer) {
		this.name = name;
		this.date = date;
		this.question = question;
		this.answer = answer;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public String getQuestion() {
		return this.question;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
}
