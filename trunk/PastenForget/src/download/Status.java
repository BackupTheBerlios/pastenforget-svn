package download;

/**
 * Diese Klasse liefert Standardstatusmeldungen.
 * 
 * @author executor
 *
 */

public class Status {
	
	/**
	 * Status fuer aktiven Download.
	 * 
	 * @return String
	 */
	public static String getActive() {
		return "Aktiv";
	}

	/**
	 * Status fuer fertigen Download.
	 * 
	 * @return String
	 */
	public static String getFinished() {
		return "Fertig";
	}

	/**
	 * Status fuer wartenden Download.
	 * 
	 * @param sec in Sekunden
	 * @return String
	 */
	public static String getWaitSec(int sec) {
		return "Warten" + "(" + new Integer(sec).toString() + " Sekunden)";
	}

	/**
	 * Status fuer wartenden Download.
	 * 
	 * @param min in Minuten
	 * @return String
	 */
	public static String getWaitMin(int min) {
		if (min == 1) {
			return "Warten" + "(" + new Integer(min).toString() + " Minute)";
		}
		return "Warten" + "(" + new Integer(min).toString() + " Minuten)";
	}

	/**
	 * Status fuer Slot belegt.
	 * 
	 * @param count Anzahl der Versuche.
	 * @return String
	 */
	public static String getNoSlot(int count) {
		if (count == 1) {
			return "Slot belegt" + "(" + new Integer(count).toString()
					+ " Versuch)";
		}
		return "Slot belegt" + "(" + new Integer(count).toString()
				+ " Versuche)";
	}
	
	/**
	 * Status fuer besondere Fehlermeldungen.
	 * 
	 * @param error Fehlermeldung
	 * @return String "Fehler: <error>"
	 */
	public static String getError(String error) {
		return "Fehler: " + error;
	}
	
	/**
	 * Status fuer gestoppten Download.
	 * 
	 * @return String
	 */
	public static String getStopped() {
		return "Download gestoppt";
	}
	
	/**
	 * Status fuer abgebrochenen Download.
	 * 
	 * @return String
	 */
	public static String getCanceled() {
		return "Download abgebrochen";
	}
}