package core.hoster;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import core.Download;
import core.DownloadInterface;
import core.Queue;
import core.ServerConnection;
import core.ServerDownload;

public class Megaupload extends Download implements DownloadInterface {

	public Megaupload(URL url, Queue queue) {
		this.setUrl(url);
		this.setQueue(queue);
		this.setStatus("Warten");
		this.setFilename("megaupload");
	}

	public WebResponse getMegauploadPage(String input) throws SAXException,
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

	public void out(String input) {
		System.out.println(input);
	}

	public String getDirectLink(String input) {
		Pattern p;
		Matcher m;
		String regex;
		String url;

		regex = "http://www93[\\s\\w/{.}{+}{'}-]*";
		p = Pattern.compile(regex);
		m = p.matcher(input);
		m.find();
		url = input.substring(m.start(), m.end());

		return url;
	}

	public String getVar(String input) {
		Pattern p;
		Matcher m;
		String regex;
		String var = new String();
		String numbers[] = new String[3];
		String target;
		int i = 0;

		/*
		 * Filtert var b = String.fromCharCode(Math.abs(-99)); var e = 'b' +
		 * String.fromCharCode(Math.sqrt(2916)); aus HTML-Dokument
		 */

		regex = "var [\\w]{1} = .*[;]{1}";
		p = Pattern.compile(regex);
		m = p.matcher(input);

		while (m.find()) {
			var += input.substring(m.start(), m.end());
		}

		/*
		 * Filtert 99, 2, 2916 aus: var b = String.fromCharCode(Math.abs(-99));
		 * var e = '2' + String.fromCharCode(Math.sqrt(2916));
		 */
		regex = "[(']{1}[-]*[\\w]+[)']";
		p = Pattern.compile(regex);
		m = p.matcher(var);

		while (m.find()) {
			numbers[i] = var.substring(m.start() + 1, m.end() - 1);
			i++;
		}

		/*
		 * Erstellt den 3 stelligen Zielstring '2' +
		 * String.fromCharCode(Math.sqrt(2916)) + String.fromCharCode(-99)
		 */

		target = numbers[1]
				+ String.valueOf((char) Math.sqrt(Double.valueOf(numbers[2])))
				+ String.valueOf((char) Math.abs(Double.valueOf(numbers[0])
						.doubleValue()));

		return target;
	}

	public String matchString(String input, String var) {
		String matched;
		Pattern p;
		Matcher m;
		String regex;

		regex = "[']+.*[']+";
		p = Pattern.compile(regex);
		m = p.matcher(input);
		m.find();
		matched = input.substring(0, m.start()) + var
				+ input.substring(m.end());

		return matched;
	}

	public void wait(String waitingtime) {
		String runningtime;
		Integer time;
		time = Integer.valueOf(waitingtime);
		try {
			for (int i = time; i > 0; i--) {
				Thread.sleep(1000);
				runningtime = String.valueOf(i);
				this.setStatus(runningtime + " Sek.");
				System.out.print(runningtime + " ");
			}
		} catch (InterruptedException ite) {
			System.out.println("Wartevorgang unterbrochen");
		}
	}

	public void downloadFileFromHoster(String link) {
		int receivedBytes;
		byte buffer[] = new byte[4096];
		FileOutputStream os;
		InputStream is;
		try {

			WebResponse pageNo1 = this.getMegauploadPage(link);
			// out( pageNo1.getText() );
			String captchaLink = "http://www.megaupload.com"
					+ pageNo1.getElementWithID("middle_top").getNode()
							.getChildNodes().item(1).getChildNodes().item(1)
							.getAttributes().getNamedItem("src").getNodeValue();
			String captcha;

			ServerConnection sc = new ServerConnection(captchaLink);
			is = sc.openDownloadStream();
			os = new FileOutputStream("captchaMU.jpg");

			while ((receivedBytes = is.read(buffer)) > 0) {
				os.write(buffer, 0, receivedBytes);
			}

			is = System.in;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			captcha = br.readLine();

			WebForm form = pageNo1.getForms()[0];

			form.setParameter("imagestring", captcha);

			String[] pnames = form.getParameterNames();
			for (int i = 0; i < pnames.length; i++) {
				String[] pvalues = form.getParameterValues(pnames[i]);
				for (int j = 0; j < pvalues.length; j++) {
					// out( pnames[i] + " : " + pvalues[j] );
				}
			}

			WebResponse pageNo2 = form.submit();
			this.out(pageNo2.getText());

			String withVars = this.getDirectLink(pageNo2.getText());
			String var = this.getVar(pageNo2.getText());

			String directLink = this.matchString(withVars, var);
			out(directLink);
			wait("46");
			if (this.stopThread.isStopped() == false) {
				serverDownload = new ServerDownload(directLink, "ebook.pdf",
						this);
				serverDownload.run();
			}
			this.stop();

		} catch (IOException ioex) {

		} catch (SAXException saex) {

		} catch (Exception ex) {

		}
	}

}
