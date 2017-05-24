package org.phymote.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.phymote.control.SmoothenData;
import org.phymote.control.exceptions.Messages;

public class SmoothenSelectDialog extends javax.swing.JDialog {
	private static final long serialVersionUID = 1L;
	private JRadioButton leftButton;
	private JLabel lengthDesc;
	private JTextField lengthTextField;
	private JButton okButton;
	private JRadioButton rightButton;
	private JRadioButton centerButton;
	private JSlider lengthSlider;
	private SmoothenSelectDialog myself = this;
	private SmoothenData result = null;

	public SmoothenSelectDialog(JFrame frame) {
		super(frame, true);
		setLocationRelativeTo(frame);
		initGUI();
		pack();
		setVisible(true);
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
			thisLayout.rowHeights = new int[] { 7, 20, 7, 7 };
			thisLayout.columnWeights = new double[] { 0.1, 0.1, 0.1 };
			thisLayout.columnWidths = new int[] { 7, 7, 7 };
			getContentPane().setLayout(thisLayout);

			leftButton = new JRadioButton();
			getContentPane().add(leftButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			leftButton.setText(Messages.getString("SmoothenSelectDialog.button_left")); //$NON-NLS-1$

			centerButton = new JRadioButton();
			getContentPane().add(centerButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			centerButton.setText(Messages.getString("SmoothenSelectDialog.button_center")); //$NON-NLS-1$
			centerButton.setSelected(true);

			rightButton = new JRadioButton();
			getContentPane().add(rightButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			rightButton.setText(Messages.getString("SmoothenSelectDialog.button_right")); //$NON-NLS-1$

			ButtonGroup orientationButtonGroup = new ButtonGroup();
			orientationButtonGroup.add(leftButton);
			orientationButtonGroup.add(centerButton);
			orientationButtonGroup.add(rightButton);

			lengthTextField = new JTextField();
			getContentPane().add(lengthTextField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			lengthTextField.setText("100"); //$NON-NLS-1$
			lengthTextField.setPreferredSize(new java.awt.Dimension(100, 22));

			lengthDesc = new JLabel();
			getContentPane().add(lengthDesc, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			lengthDesc.setText(Messages.getString("SmoothenSelectDialog.label_length")); //$NON-NLS-1$

			lengthSlider = new JSlider(0, 5000, 100);
			getContentPane().add(lengthSlider, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

			lengthSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					lengthTextField.setText("" + ((JSlider) e.getSource()).getValue()); //$NON-NLS-1$
				}
			});

			okButton = new JButton();
			getContentPane().add(okButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			okButton.setText(Messages.getString("SmoothenSelectDialog.button_ok")); //$NON-NLS-1$

			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					myself.generateResult();
					myself.dispose();
				}
			});

			this.setSize(211, 159);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void generateResult() {
		int myOrientation = (centerButton.isSelected() ? SmoothenData.CENTER : leftButton.isSelected() ? SmoothenData.LEFT : SmoothenData.RIGHT);
		result = new SmoothenData(myOrientation, SmoothenData.ARITHMETIC, Integer.parseInt(lengthTextField.getText()));
	}

	public SmoothenData getResult() {
		return result;
	}

	public boolean hasResult() {
		return (result != null);
	}
}
