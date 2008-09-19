package core.decrypt;

import java.io.FileOutputStream;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 2) {
			if(args[0].equals("-link")) {
				Decrypt dc = new DDLWarez( args[1] );
				dc.decrypt();
				
				
			} else {
				System.out.println("Bitte Parameter der Form \"ddlwarez.jar -link <ddlwarezlink>\" angeben");
			}
		} else {
			System.out.println("Bitte Parameter der Form \"ddlwarez.jar -link <ddlwarezlink>\" angeben");
		}
		// String url = "http://www.ddl-warez.org/detail.php?id=16691&cat=movies";
		// Decrypt dc = new DDLWarez( url ); 
		// dc.decrypt();
	}

}
