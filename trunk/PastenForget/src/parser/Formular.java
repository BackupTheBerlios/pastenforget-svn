package parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Diese Klasse beinhaltet alle Parameter, die für einen POST notwendig sind.
 * 
 * Beispiel:
 *   <form action="hoster.php" method="POST">
 *   	<input type="hidden" name="id" value="0815" />
 *   	<input type="text" name="captchaCode" />
 *   	<input type="hidden" name="" value="nutzlos" />
 *   	<input type="submit" value="senden" />
 *   </form>
 *   
 *   String action = "hoster.php"
 *   String method = "POST"
 *   Map<String, String> postParameters  {
 *      ("id", "0815"),
 *      ("captchaCode", "")
 *   }
 * 
 * @author christopher
 *
 */
public class Formular {
	private final Map<String, String> postParameters = new HashMap<String, String>();
	private final String action;
	private final String method;
	
	/**
	 * Von einem gegebenen Formular (Datentyp: Tag) werden alle Informationen extrahiert,
	 *  und die entsprechenden privaten Variablen gesetzt.
	 * @param formular
	 */
	public Formular(Tag formular) {
		this.action = formular.getAttribute("action");
		this.method = formular.getAttribute("method");
		List<Tag> inputFields = formular.getSimpleTag("input");
		for(Tag inputField : inputFields) {
			String name = inputField.getAttribute("name");
			String value = inputField.getAttribute("value");
			if(name != null && !name.equals("")) {
				value = (value != null) ? value : new String();
				this.postParameters.put(name, value);
			}
		}		
	}
	
	/**
	 * Getter für POST Parameter
	 *  Key: <input name="">
	 *  Value: <input value="">
	 * @return
	 */
	public Map<String, String> getPostParameters() {
		return postParameters;
	}
	
	/**
	 * Getter für Action
	 * @return
	 */
	public String getAction() {
		return action;
	}
	
	/**
	 * Getter für Method (ie. GET oder POST)
	 * @return
	 */
	public String getMethod() {
		return method;
	}
}
