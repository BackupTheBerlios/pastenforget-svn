package filtration;

import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

public class DDLWarez extends Filtration {
	
	class Links extends Thread {
		private WebForm current;
		
		public Links( WebForm current ) {
			this.current = current;
			this.getLink();
		}
		
		public void run() {
			this.getLink();
		}
		
		private void getLink() {
			WebResponse wr;
			try {
				wr = this.current.submit();
				String directlink = wr.getElementsWithName("second")[0].getAttribute("src");
				out( directlink );
			} catch(SAXException saex) {
				
			} catch(IOException ioex) {
				
			}
		}
	}
	
	
	
	public DDLWarez( String url ) {
		super( url );
	}

	@Override
	public List<String> decrypt() {
		try {
			// List<String> directlinks = new ArrayList<String>();
			WebResponse pageNo1 = getDDLWarezPage( this.url );
			WebForm[] webforms = pageNo1.getForms();
			
			for(int i = 0; i < webforms.length; i++) {
				if( webforms[i].getAttribute("name").indexOf("dl") == 0) {
					WebResponse wr;
					wr = webforms[i].submit();
					String directlink = wr.getElementsWithName("second")[0].getAttribute("src");
					out( directlink );
				}
			}
			
		} catch(SAXException saex) {
			
		} catch(IOException ioex) {
			
		}
		
		return null;
	}

	
	private WebResponse getDDLWarezPage( String input ) throws SAXException, IOException {
		WebConversation conversation = new WebConversation();
		WebRequest request;
		WebResponse response;
		
		HttpUnitOptions.setScriptingEnabled( false ); // deaktiviert Javascript der empfangenen Seite
		request = new GetMethodWebRequest( input );
		response = conversation.getResponse( request );
		
		return response;
	}
	
	
	
	
	
	
}
