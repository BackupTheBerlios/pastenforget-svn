package algorithms;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class GUI extends JFrame {
	private JScrollPane jScrollPane1;
	private JLabel banner;
	private JButton cancel;
	private JButton loadContainer;
	private JButton download;

	public GUI() {
		this.initGUI();
		this.setVisible(true);
	}
	
	private void initGUI() {
		try {
			{
				this.setTitle("FileHosterDownloader");
				getContentPane().setLayout(null);
				this.setFocusable(false);
				{
					ScrolledJTable jTable = new ScrolledJTable(new String[] { "Dateiname", "Dateigröße", "Hoster", "Fortschritt" }); 
					jTable.setBounds(12, 71, 485, 323);
					jTable.setColumnWidth("Dateiname", 200);
					jTable.setColumnWidth("Dateigröße", 90);
					jTable.setColumnWidth("Hoster", 125);
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					jTable.addRow(new Object[] { "A", "B", "C", "D"});
					
					
					getContentPane().add(jTable);
					jTable.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent evt) {
							jTableKeyPressed(evt);
						}
					});
				}
				{
					download = new JButton();
					getContentPane().add(download);
					download.setText("Download");
					download.setBounds(12, 406, 149, 22);
				}
				{
					loadContainer = new JButton();
					getContentPane().add(loadContainer);
					loadContainer.setText("Container öffnen");
					loadContainer.setBounds(191, 406, 148, 22);
				}
				{
					cancel = new JButton();
					getContentPane().add(cancel);
					cancel.setText("Abbrechen");
					cancel.setBounds(374, 406, 122, 22);
				}
				{
					banner = new JLabel();
					getContentPane().add(banner);
					banner.setText("File Hoster Downloader");
					banner.setBounds(138, 19, 258, 23);
					banner.setFont(new java.awt.Font("Segoe UI",1,22));
				}
			}
			{
				this.setSize(519, 500);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jTableKeyPressed(KeyEvent evt) {
		System.out.println("jTable.keyPressed, event="+evt);
		//TODO add your code for jTable.keyPressed
	}

}
