package algorithms;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ScrolledJTable extends JScrollPane {
	private static final long serialVersionUID = 0;

/* ------------------------------------------------------- */
	class jTable extends JTable {
		private static final long serialVersionUID = 0;
		private boolean press = false;
		
		public jTable(TableModel dm) {
			super(dm);
		}
		@Override
		public boolean isCellEditable(int x, int y) {
			return false;
		}
		
		@Override
		protected boolean processKeyBinding(KeyStroke stroke, KeyEvent evt, int condition, boolean pressed) {
				if((evt.toString().indexOf("KEY_PRESSED") > 0) && (evt.toString().indexOf("keyCode=127") > 0)) {
					if(press == true) {
						System.out.println(evt);
						int[] rows = this.getSelectedRows();
						System.out.println(rows.length);
						for(int i = 0; i < rows.length; i++) {
							((DefaultTableModel)this.getModel()).removeRow(rows[i]);
						}
						return super.processKeyBinding(stroke, evt, condition, pressed);
					}
					press = true;
				} else {
					press = false;
				}
				return false;
		}
	}
/* ------------------------------------------------------- */
	
	private JTable table = null;
	private DefaultTableModel model = null;
	private String[] columnNames = null;

	public ScrolledJTable() {
		super();
		this.setViewportView(table);
		this.table = new jTable(model);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				downloadKeyPressed(evt);
			}
		});
	}
	
	public ScrolledJTable(String[] columnNames) {
		super();
		this.columnNames = columnNames;
		this.model = new DefaultTableModel( null, this.columnNames );
		this.table = new jTable(model);
		this.setViewportView(this.table);
		
	}
	
	public void setColumnNames( String[] columnNames ) {
		this.columnNames = columnNames;
		this.model = new DefaultTableModel( null, this.columnNames );
	}
	
	public void setColumnWidth(String columnName, int preferredWidth) {
		this.table.getColumn(columnName).setPreferredWidth(preferredWidth);
	}
	
	public void addRow(Object[] rowData) {
		this.model.addRow(rowData);
	}
	
	private void downloadKeyPressed(KeyEvent evt) {
		System.out.println("download.keyPressed, event="+evt);
		//TODO add your code for download.keyPressed
	}
}
