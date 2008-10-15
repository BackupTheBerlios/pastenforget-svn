package parser;

import java.util.HashMap;
import java.util.Map;

public class FormProperties {
	private final Map<String, String> parameters;
	private String action;

	/**
	 * Klasse, welche aus den Request-Parametern und dem Action-Attribut besteht.
	 */
	public FormProperties(Map<String, String> parameters, String action) {
		this.parameters = (parameters == null) ? new HashMap<String, String>()
				: parameters;
		this.action = action;
	}
	
	/**
	 * Klasse, welche aus den Request-Parametern und dem Action-Attribut besteht.
	 */
	public FormProperties(String action) {
		this.action = action;
		this.parameters = new HashMap<String, String>();
	}

	/**
	 * Setzt das Attribut action.
	 * @param action
	 */
	public void setAction(String action) {
		this.action = action;
	}
	
	/**
	 * Fügt einen Parameter hinzu.
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, String value) {
		this.parameters.put(name, value);
	}
	
	/**
	 * Gibt die Parameter zurück.
	 * @return parameters
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * Gibt das Action-Attribut zurück.
	 * @return
	 */
	public String getAction() {
		return this.action;
	}
}
