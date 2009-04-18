package download;

import settings.Languages;

/**
 * Diese Klasse liefert Standardstatusmeldungen.
 * 
 * @author executor
 * 
 */

public class Status {

	/**
	 * Status fuer gestarteten Download.
	 * 
	 * @return String
	 */
	public static String getStarted() {
		return Languages.getTranslation("DownloadStarted");
	}

	/**
	 * Status fuer aktiven Download.
	 * 
	 * @return String
	 */
	public static String getActive() {
		return Languages.getTranslation("Active");
	}

	/**
	 * Status fuer fertigen Download.
	 * 
	 * @return String
	 */
	public static String getFinished() {
		return Languages.getTranslation("DownloadFinished");
	}

	/**
	 * Status fuer wartenden Download.
	 * 
	 * @return String
	 */
	public static String getWaiting() {
		return Languages.getTranslation("Waiting");
	}

	/**
	 * Status fuer wartenden Download.
	 * 
	 * @param sec
	 *            in Sekunden
	 * @return String
	 */
	public static String getWaitSec(int sec) {
		if (sec == 1) {
			return Languages.getTranslation("Waiting") + " ("
					+ new Integer(sec).toString() + " "
					+ Languages.getTranslation("second") + ")";
		}
		return Languages.getTranslation("waiting") + " ("
				+ new Integer(sec).toString() + " "
				+ Languages.getTranslation("seconds") + ")";
	}

	/**
	 * Status fuer wartenden Download.
	 * 
	 * @param min
	 *            in Minuten
	 * @return String
	 */
	public static String getWaitMin(int min) {
		if (min == 1) {
			return Languages.getTranslation("Waiting") + " ("
					+ new Integer(min).toString() + " "
					+ Languages.getTranslation("minute") + ")";
		}
		return Languages.getTranslation("Waiting") + " ("
				+ new Integer(min).toString() + " "
				+ Languages.getTranslation("minutes") + ")";
	}

	/**
	 * Status fuer Slot belegt.
	 * 
	 * @param count
	 *            Anzahl der Versuche.
	 * @return String
	 */
	public static String getNoSlot(int count) {
		if (count == 1) {
			return Languages.getTranslation("NoSlot") + " ("
					+ new Integer(count).toString() + " "
					+ Languages.getTranslation("try") + ")";
		}
		return Languages.getTranslation("NoSlot") + " ("
				+ new Integer(count).toString() + " "
				+ Languages.getTranslation("tries") + ")";
	}

	/**
	 * Status fuer besondere Fehlermeldungen.
	 * 
	 * @param error
	 *            Fehlermeldung
	 * @return String "Fehler: <error>"
	 */
	public static String getError(String error) {
		return Languages.getTranslation("Error") + ": " + error;
	}

	/**
	 * Status fuer gestoppten Download.
	 * 
	 * @return String
	 */
	public static String getStopped() {
		return Languages.getTranslation("DownloadStopped");
	}

	/**
	 * Status fuer abgebrochenen Download.
	 * 
	 * @return String
	 */
	public static String getCanceled() {
		return Languages.getTranslation("DownloadCanceled");
	}
}
