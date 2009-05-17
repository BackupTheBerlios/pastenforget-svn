package download.rapidshare;

import download.Download;
import download.IHosterState;


public enum RapidshareStates implements IHosterState {
	WAITINGTIME("") {
		@Override public void fireEvent(Download download) {
			download.restart();
            System.out.println("IP Loading"); 
       }
		
	},
	FILEDOWN("") {
		@Override public void fireEvent(Download download) { 
            System.out.println("File Down");
			download.cancel(); 
       }
	},
	IPLOADING("") {
		@Override public void fireEvent(Download download) { 
			System.out.println("IP Loading");
            download.restart();
       }
	};
	
	private RapidshareStates(String pattern) {
		this.pattern = pattern;
	}
	
	private String pattern;
		
	public String getPattern() {
		return this.pattern;
	}
	
}
