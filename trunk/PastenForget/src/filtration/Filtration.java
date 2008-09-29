package filtration;

import java.util.List;

public class Filtration {
	protected String url;
	
	public Filtration( String url ) {
		this.url = url;
	}
	
	public List<String> decrypt() {
		return null;
	}
	
	
	public void setURl( String url ) {
		this.url = url;
	}
	
	public String getURL() {
		return this.url;
	}
	
	protected void out( Object input ) {	
		System.out.println( input.toString() );
	}

}
