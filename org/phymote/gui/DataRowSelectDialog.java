package org.phymote.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.phymote.control.exceptions.Messages;
import org.phymote.sensor.datarow.ChartModificationDataRow;

/**
 * Dialogwindow to select the Datarows you're interrested in.
 * 
 * @author Christoph Krichenbauer
 * 
 */
public class DataRowSelectDialog extends javax.swing.JDialog {
	private static final long serialVersionUID = -9179832974195300406L;
	private JScrollPane SelectBoxScrollPane;
	private JCheckBox dataSelectBox[];
	private JLabel TitleLabel;
	private JButton CancelButton;
	private JButton OKButton;
	private JPanel ButtonPanel;
	private ChartModificationDataRow dataRow;
	private DataRowSelectDialog myself = this;

	private boolean[] result = null;
	private boolean hasResult = false;

	public DataRowSelectDialog(JFrame frame, ChartModificationDataRow myDataRow) {
		super(frame, true);
		setLocationRelativeTo(frame);
		dataRow = myDataRow;
		initGUI();
		pack();
		setVisible(true);
	}

	private void initGUI() {
		try {
			SelectBoxScrollPane = new JScrollPane();
			getContentPane().add(SelectBoxScrollPane, BorderLayout.CENTER);

			JPanel SelectBoxPanel = new JPanel();
			SelectBoxPanel.setLayout(new BoxLayout(SelectBoxPanel, BoxLayout.Y_AXIS));

			SelectBoxScrollPane.setViewportView(SelectBoxPanel);

			dataSelectBox = new JCheckBox[dataRow.getDimension()];
			for (int i = 0; i < dataRow.getDimension(); i++) {
				dataSelectBox[i] = new JCheckBox();
				dataSelectBox[i].setText(dataRow.getDesc(i));
				dataSelectBox[i].setSelected(true);
				SelectBoxPanel.add(dataSelectBox[i]);

			}

			ButtonPanel = new JPanel();
			FlowLayout ButtonPanelLayout = new FlowLayout();
			ButtonPanelLayout.setAlignment(FlowLayout.RIGHT);
			getContentPane().add(ButtonPanel, BorderLayout.SOUTH);
			ButtonPanel.setLayout(ButtonPanelLayout);

			CancelButton = new JButton();
			ButtonPanel.add(CancelButton);
			CancelButton.setText(Messages.getString("DataRowSelectDialog.button_cancel")); //$NON-NLS-1$
			{
				OKButton = new JButton();
				ButtonPanel.add(OKButton);
				OKButton.setText(Messages.getString("DataRowSelectDialog.button_ok")); //$NON-NLS-1$
				OKButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						myself.generateResult();
						myself.dispose();
					}
				});
			}
			CancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					myself.dispose();
				}
			});

			TitleLabel = new JLabel();
			getContentPane().add(TitleLabel, BorderLayout.NORTH);
			TitleLabel.setText(Messages.getString("DataRowSelectDialog.label_select_data")); //$NON-NLS-1$
			this.setSize(219, 289);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateResult() {
		result = new boolean[dataSelectBox.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = dataSelectBox[i].isSelected();
		}
		hasResult = true;
	}

	/**
	 * Check if any result exists
	 * 
	 * @return true, if result exists
	 */
	public boolean hasResult() {
		return hasResult;
	}

	/**
	 * Generate Resultarray
	 * 
	 * @return array of "selected"s
	 */
	public boolean[] getResult() {
		return result;
	}

}
