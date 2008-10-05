package download.hoster;

import java.util.Iterator;
import java.util.List;

import parser.Parser;
import parser.Request;

public class Hoster {
	
	public static Request readRequestFormular(String requestForm) {
		Request request = new Request();
		List<String> input = Parser.getSimpleTag("input", requestForm);
		Iterator<String> inputIt = input.iterator();
		while (inputIt.hasNext()) {
			String currentInput = inputIt.next();
			String name = new String();
			String value = new String();
			if ((name = Parser.getAttribute("name", currentInput)) != null) {
				value = Parser.getAttribute("value", currentInput);
				request.addParameter(name, value);
			}
		}
		return request;
	}
}
