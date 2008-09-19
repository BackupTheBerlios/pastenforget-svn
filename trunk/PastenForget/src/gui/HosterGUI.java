package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import middleware.Middleware;
import core.Queue;

/**
 * Gemeinsame GUI-Attribute aller Hoster.
 * 
 * @author cpieloth
 * 
 */
public class HosterGUI extends JFrame implements Observer, ActionListener {

	private static final long serialVersionUID = -7775517952036303028L;

	protected JLabel hosterLabel, processLabel, waitLabel;

	protected JTextField processTextfield, waitTextfield, statusTextfield;

	protected JButton cancelButton;

	protected Middleware middleware;
	
	protected Queue queue;

	Container c;

	public HosterGUI() {

		c = getContentPane();
		c.setLayout(null);

		// Labels erstellen
		hosterLabel = new JLabel();
		hosterLabel.setLocation(25, 10);
		hosterLabel.setSize(150, 25);
		c.add(hosterLabel);

		processLabel = new JLabel("In Arbeit:");
		processLabel.setLocation(25, 40);
		processLabel.setSize(150, 25);
		c.add(processLabel);

		waitLabel = new JLabel("Queue:");
		waitLabel.setLocation(25, 70);
		waitLabel.setSize(150, 25);
		c.add(waitLabel);

		// TextFields erstellen
		processTextfield = new JTextField();
		processTextfield.setBackground(Color.white);
		processTextfield.setSize(225, 25);
		processTextfield.setLocation(175, 40);
		processTextfield.setText("Kein Download gestartet!");
		c.add(processTextfield);

		statusTextfield = new JTextField();
		statusTextfield.setBackground(Color.white);
		statusTextfield.setSize(50, 25);
		statusTextfield.setLocation(425, 40);
		statusTextfield.setText("0,0%");
		statusTextfield.setHorizontalAlignment(JTextField.RIGHT);
		c.add(statusTextfield);

		waitTextfield = new JTextField();
		waitTextfield.setBackground(Color.white);
		waitTextfield.setSize(225, 25);
		waitTextfield.setLocation(175, 70);
		waitTextfield.setText("Kein Download gestartet!");
		c.add(waitTextfield);

		// Buttons erstellen
		cancelButton = new JButton("Abbrechen");
		cancelButton.setSize(120, 25);
		cancelButton.setLocation(500, 40);
		cancelButton.setEnabled(true);
		c.add(cancelButton);
	}
	
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();
		System.out.println("'" + source + "' performed");
		if ("Abbrechen".equals(source)) {
			middleware.cancel(queue);
		}
	}
	
	public void update(Observable arg0, Object arg1) {
		if (arg1.equals("queue")) {
			processTextfield.setText(queue.getCurrent().getFilename());
			statusTextfield.setText(queue.getCurrent().getStatus());
			queue.getCurrent().addObserver(this);
			waitTextfield.setText(queue.getNext().getFilename());
		}
		if (arg1.equals("status")) {
			statusTextfield.setText(queue.getCurrent().getStatus());
		}
	}

}
