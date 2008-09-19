package core.hoster;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import core.Download;
import core.DownloadInterface;
import core.Queue;
import core.ServerConnection;

public class Netload extends Download implements DownloadInterface {

	public Netload(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFilename("netload");
	}

	public static WebResponse getNetloadPage(String input) throws SAXException,
			IOException {
		WebConversation conversation = new WebConversation();
		WebRequest request;
		WebResponse response;

		HttpUnitOptions.setScriptingEnabled(false); // deaktiviert Javascript
													// der empfangenen Seite
		request = new GetMethodWebRequest(input);
		response = conversation.getResponse(request);

		return response;
	}

	public static String getLinkToNetloadPageNo2(WebResponse input)
			throws SAXException {
		String link;

		link = input.getElementsByTagName("FRAME")[0].getAttribute("src");

		return link;
	}

	public static WebLink getLinkToNetloadPageNo3(WebResponse input)
			throws SAXException {
		WebLink[] weblinks;
		WebLink link;
		int i;

		weblinks = input.getLinks();
		i = 0;

		while (weblinks[i].getAttribute("href").indexOf("index.php?id=10") < 0) {
			i++;
		}

		link = weblinks[i];

		return link;
	}

	public static void out(String input) {
		System.out.println(input);
	}

	public static void main(String[] args) {
		try {
			WebResponse pageNo1, pageNo2, pageNo3;
			String linkToPageNo2;
			WebLink linkToPageNo3;
			String captchaLink;
			/*--------------------------------*/
			InputStream is;
			FileOutputStream os;
			int receivedBytes;
			byte buffer[] = new byte[4096];
			/*--------------------------------*/

			pageNo1 = getNetloadPage("http://download.serienjunkies.org/go-0a3369f917fead5cbf5416a51225c2553268c8a84464b319e49b11cb733818e20884f8926d8c252a02ffe9cc0eb36221a23c1a127969ce5e503dcf4358e1304d289b2fbed848c3cae723cdec8db45d033e5fcf0c8729ee8f/?");
			out(pageNo1.getText());
			linkToPageNo2 = getLinkToNetloadPageNo2(pageNo1);
			pageNo2 = getNetloadPage(linkToPageNo2);
			linkToPageNo3 = getLinkToNetloadPageNo3(pageNo2);
			pageNo3 = linkToPageNo3.click();
			// out( pageNo3.getText() );
			WebForm download = pageNo3.getForms()[0];
			captchaLink = "http://netload.in/"
					+ download.getDOMSubtree().getChildNodes().item(1)
							.getChildNodes().item(5).getChildNodes().item(1)
							.getAttributes().getNamedItem("src").getNodeValue();
			// out( captchaLink );
			ServerConnection sc = new ServerConnection(captchaLink);
			is = sc.openDownloadStream();
			os = new FileOutputStream("captcha.jpg");

			while ((receivedBytes = is.read(buffer)) > 0) {
				os.write(buffer, 0, receivedBytes);
			}

			System.out.println("Bitte geben Sie das Captcha ein");
			is = System.in;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String captcha = br.readLine();
			// out( captcha );

			// download.setParameter("captcha_check", captcha);
			// download.submit();
			// download.newUnvalidatedRequest();

			download.getParameter("file_id");
			download.getParameter("start");
			WebConversation conversation = new WebConversation();
			WebRequest request;
			WebResponse response;
			String[] names = new String[] { "dl.start" };
			String[] values = new String[] { "Free" };
			request = new PostMethodWebRequest("http://netload.in/"
					+ download.getAction());
			request.setParameter("file_id", "start");
			request.setParameter("captcha_check", captcha);
			request.setParameter("start", "");
			response = conversation.getResponse(request);
			out(response.getText());
			/*
			 * SubmitButton sb = download.getSubmitButtons()[0]; WebResponse wr =
			 * download.submit( sb ); is = wr.getInputStream(); os = new
			 * FileOutputStream("test.test");
			 * 
			 * while( (receivedBytes = is.read(buffer)) > 0) { os.write( buffer,
			 * 0, receivedBytes ); }
			 */

		} catch (IOException ioex) {

		} catch (SAXException saex) {

		}
	}

	@Override
	public boolean start() {
		// TODO Download starten
		return false;
	}

	public void downloadFileFromHoster(String url) {
		// TODO Auto-generated method stub

	}

}
