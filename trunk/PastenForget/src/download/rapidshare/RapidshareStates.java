package download.rapidshare;

import parser.Tag;
import download.Download;
import download.IHosterState;
import download.Status;


public enum RapidshareStates implements IHosterState {
	WAITINGTIME("try again in") {
		@Override public void fireEvent(Download download) {
            Tag currentPage = download.getCurrentPage();
			Tag div = currentPage.getElementsByClass("div", "klappbox").get(0);
			
			
			
			
			
			System.out.println("RapidshareState: IP Loading"); 
			download.restart();
       }
		
	},
	FILEDOWN("not be found") {
		@Override public void fireEvent(Download download) { 
            System.out.println("RapidshareState: File Down");
			download.cancel(); 
       }
	},
	IPLOADING("already downloading") {
		@Override public void fireEvent(Download download) { 
			System.out.println("RapidshareState: IP Loading");
			download.setStatus(Status.getNoSlot(0));
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
