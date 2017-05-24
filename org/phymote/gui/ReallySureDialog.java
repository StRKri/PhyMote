package org.phymote.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.phymote.control.exceptions.Messages;

public class ReallySureDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel textLabel;
	private JButton cancelButton;
	private JButton okButton;
	private boolean sure = false;
	private ReallySureDialog mySelf = this;
	private String text;

	public ReallySureDialog(JFrame frame, String myText) {
		super(frame, true);
		text = myText;
		setLocationRelativeTo(frame);
		initGUI();
		pack();
		setVisible(true);
	}

	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] { 0.1, 0.1 };
		thisLayout.rowHeights = new int[] { 7, 7 };
		thisLayout.columnWeights = new double[] { 0.1, 0.1 };
		thisLayout.columnWidths = new int[] { 7, 7 };
		getContentPane().setLayout(thisLayout);
		textLabel = new JLabel();
		getContentPane().add(textLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		textLabel.setText(text);
		okButton = new JButton();
		getContentPane().add(okButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		okButton.setText(Messages.getString("ReallySureDialog.OKButton")); //$NON-NLS-1$
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sure = true;
				mySelf.close();
			}
		});
		cancelButton = new JButton();
		getContentPane().add(cancelButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		cancelButton.setText(Messages.getString("ReallySureDialog.CancelButton")); //$NON-NLS-1$
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mySelf.close();
			}
		});
		this.setSize(242, 162);
	}

	public void close() {
		dispose();
	}

	public boolean hasResult() {
		return true;
	}

	public boolean getResult() {
		return sure;
	}

}
