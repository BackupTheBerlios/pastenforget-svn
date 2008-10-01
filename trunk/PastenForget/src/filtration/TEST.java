package filtration;

	import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
public class TEST {
	  public static void main( String args[] ) throws IOException {
		  InputStream in = new URL("http://dl10.chip.de/download/25a1a786d5d84a8bb93f6788385510e7/48e3ba38/34113/Nero-9.0.9.4b_trial.exe").openConnection().getInputStream();
		  OutputStream out = new FileOutputStream("nero.exe");
		  int nextChar;
		  int received = 0;
		  while ((nextChar = in.read()) != -1) {
			  	out.write( Character.toUpperCase((char)nextChar));
			  	received++;
			  	if(received%10000 == 0) {
			  		System.out.println(received);
			  	}
		  }
			  	out.write('\n');
		  		out.flush();
	  }
}

