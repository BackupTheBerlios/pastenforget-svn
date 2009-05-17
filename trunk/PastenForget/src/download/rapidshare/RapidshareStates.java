package download.rapidshare;

import download.Download;
import download.IHosterState;


public enum RapidshareStates implements IHosterState {
	WAITINGTIME("") {
		@Override public void fireEvent(Download download) {
            System.out.println("RapidshareState: IP Loading"); 
			download.restart();
       }
		
	},
	FILEDOWN("") {
		@Override public void fireEvent(Download download) { 
            System.out.println("RapidshareState: File Down");
			download.cancel(); 
       }
	},
	IPLOADING("") {
		@Override public void fireEvent(Download download) { 
			System.out.println("RapidshareState: IP Loading");
            download.restart();
       }
	};
	
	private final String PATTERN;
	
	private RapidshareStates(String pattern) {
		this.PATTERN = pattern;
	}

	public String getPattern() {
		return this.PATTERN;
	}
	
}
