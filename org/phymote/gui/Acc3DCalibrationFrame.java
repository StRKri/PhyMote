package org.phymote.gui;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import org.phymote.control.exceptions.Messages;
import org.phymote.sensor.Acc3DSensor;
import org.phymote.sensor.datarow.Acc3DDataRow;

/**
 * @author Christoph Krichenbauer Calibration wizard's Frame. Used to calibrate
 *         WiiMote manually.
 */
public class Acc3DCalibrationFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private final int minMoteAcc = 0;
	private final int maxMoteAcc = 255;

	private JPanel jContentPane = null;
	private JTabbedPane jTabbedPane = null;
	private JPanel statusPanel = null;
	private JLabel labelX = null;
	private JLabel labelY = null;
	private JLabel labelZ = null;
	private JLabel labelXRange = null;
	private JLabel labelYRange = null;
	private JLabel labelZRange = null;
	private JRadioButton radioUnitG = null;
	private JLabel labelUnit = null;
	private JLabel labelUnitG = null;
	private JRadioButton radioUnitMss = null;
	private JLabel labelUnitMss = null;
	private JPanel calibrateXPanel = null;
	private JLabel Icon = null;
	private JLabel Headline = null;
	private JTextPane HowTo = null;
	private JButton cancelButton = null;
	private JButton nextButton = null;
	private JButton calibrateButton = null;
	private JPanel calibrateYPanel = null;
	private JLabel Icon1 = null;
	private JLabel Headline1 = null;
	private JTextPane HowTo1 = null;
	private JButton cancelButton1 = null;
	private JButton nextButton1 = null;
	private JPanel calibrateZPanel = null;
	private JLabel Icon11 = null;
	private JLabel Headline11 = null;
	private JTextPane HowTo11 = null;
	private JButton cancelButton11 = null;
	private JButton nextButton11 = null;

	private Acc3DSensor mote;
	private Acc3DDataRow[] calibrationdata;

	public Acc3DCalibrationFrame(Acc3DSensor acc) {
		super();
		mote = acc;
		calibrationdata = new Acc3DDataRow[3];
		initialize();
		calculateConfig();
	}

	private void calculateConfig() {
		int gIntervalX = Math.abs(mote.getCalibrationGravity(0));
		float gMinuxX = ((float) mote.getCalibrationZero(0) - (float) minMoteAcc) / gIntervalX;
		float gPlusX = ((float) maxMoteAcc - (float) mote.getCalibrationZero(0)) / gIntervalX;
		float resolutionX = (Math.abs(gPlusX) + Math.abs(gMinuxX)) / (maxMoteAcc - minMoteAcc);
		labelXRange.setText("-" + Float.toString((float) Math.round(gMinuxX * 10) / 10) + " g – " + Float.toString((float) Math.round(gPlusX * 10) / 10) + "g ± "
				+ Float.toString((float) Math.round(resolutionX * 10000) / 10000) + " g");

		int gIntervalY = Math.abs(mote.getCalibrationGravity(1));
		float gMinuxY = ((float) mote.getCalibrationZero(1) - (float) minMoteAcc) / gIntervalY;
		float gPlusY = ((float) maxMoteAcc - (float) mote.getCalibrationZero(1)) / gIntervalY;
		float resolutionY = (Math.abs(gPlusY) + Math.abs(gMinuxY)) / (maxMoteAcc - minMoteAcc);
		labelYRange.setText("-" + Float.toString((float) Math.round(gMinuxY * 10) / 10) + " g – " + Float.toString((float) Math.round(gPlusY * 10) / 10) + "g ± "
				+ Float.toString((float) Math.round(resolutionY * 10000) / 10000) + " g");

		int gIntervalZ = Math.abs(mote.getCalibrationGravity(2));
		float gMinuxZ = ((float) mote.getCalibrationZero(2) - (float) minMoteAcc) / gIntervalZ;
		float gPlusZ = ((float) maxMoteAcc - (float) mote.getCalibrationZero(2)) / gIntervalZ;
		float resolutionZ = (Math.abs(gPlusZ) + Math.abs(gMinuxZ)) / (maxMoteAcc - minMoteAcc);
		labelZRange.setText("-" + Float.toString((float) Math.round(gMinuxZ * 10) / 10) + " g – " + Float.toString((float) Math.round(gPlusZ * 10) / 10) + "g ± "
				+ Float.toString((float) Math.round(resolutionZ * 10000) / 10000) + " g");

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(215, 266);
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setClosable(true);
		setContentPane(getJContentPane());
		setVisible(true);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			jContentPane = new JPanel();
			jContentPane.setLayout(gridLayout);
			jContentPane.add(getJTabbedPane(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setEnabled(false);
			jTabbedPane.addTab(Messages.getString("Acc3DCalibrationFrame.tab_status"), null, getStatusPanel(), null); //$NON-NLS-1$
			jTabbedPane.addTab(Messages.getString("Acc3DCalibrationFrame.tab_x"), null, getCalibrateXPanel(), null); //$NON-NLS-1$
			jTabbedPane.addTab(Messages.getString("Acc3DCalibrationFrame.tab_y"), null, getCalibrateYPanel(), null); //$NON-NLS-1$
			jTabbedPane.addTab(Messages.getString("Acc3DCalibrationFrame.tab_z"), null, getCalibrateZPanel(), null); //$NON-NLS-1$
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes statusPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStatusPanel() {
		if (statusPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridwidth = 2;
			gridBagConstraints9.gridy = 9;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 1;
			gridBagConstraints13.gridy = 7;
			labelUnitMss = new JLabel();
			labelUnitMss.setText(Messages.getString("Acc3DCalibrationFrame.unit_m_per_s2")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridy = 7;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 6;
			labelUnitG = new JLabel();
			labelUnitG.setText(Messages.getString("Acc3DCalibrationFrame.label_unit_g")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 1;
			gridBagConstraints10.gridy = 5;
			labelUnit = new JLabel();
			labelUnit.setFont(new Font("Dialog", Font.BOLD, 14));
			labelUnit.setText(Messages.getString("Acc3DCalibrationFrame.label_units")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridy = 6;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 3;
			labelZRange = new JLabel();
			labelZRange.setText("-3.0 g – 3.0g± 0.03g");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 2;
			labelYRange = new JLabel();
			labelYRange.setText("-3.0 g – 3.0g± 0.03g");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			labelXRange = new JLabel();
			labelXRange.setText("-3.0 g – 3.0g± 0.03g");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 3;
			labelZ = new JLabel();
			labelZ.setText(Messages.getString("Acc3DCalibrationFrame.label_Z")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 2;
			labelY = new JLabel();
			labelY.setText(Messages.getString("Acc3DCalibrationFrame.label_Y")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			labelX = new JLabel();
			labelX.setText(Messages.getString("Acc3DCalibrationFrame.label_X")); //$NON-NLS-1$
			statusPanel = new JPanel();
			statusPanel.setLayout(new GridBagLayout());
			statusPanel.add(labelX, gridBagConstraints2);
			statusPanel.add(labelY, gridBagConstraints3);
			statusPanel.add(labelZ, gridBagConstraints4);
			statusPanel.add(labelXRange, gridBagConstraints5);
			statusPanel.add(labelYRange, gridBagConstraints6);
			statusPanel.add(labelZRange, gridBagConstraints7);
			// statusPanel.add(getRadioUnitG(), gridBagConstraints8);
			// statusPanel.add(labelUnit, gridBagConstraints10);
			// statusPanel.add(labelUnitG, gridBagConstraints11);
			// statusPanel.add(getRadioUnitMss(), gridBagConstraints12);
			// statusPanel.add(labelUnitMss, gridBagConstraints13);
			statusPanel.add(getCalibrateButton(), gridBagConstraints9);
		}
		return statusPanel;
	}

	/**
	 * This method initializes radioUnitG
	 * 
	 * @return javax.swing.JRadioButton
	 */
	@SuppressWarnings("unused")
	private JRadioButton getRadioUnitG() {
		if (radioUnitG == null) {
			radioUnitG = new JRadioButton();
			radioUnitG.setEnabled(false);
		}
		return radioUnitG;
	}

	/**
	 * This method initializes radioUnitMss
	 * 
	 * @return javax.swing.JRadioButton
	 */
	@SuppressWarnings("unused")
	private JRadioButton getRadioUnitMss() {
		if (radioUnitMss == null) {
			radioUnitMss = new JRadioButton();
			radioUnitMss.setEnabled(false);
		}
		return radioUnitMss;
	}

	/**
	 * This method initializes calibrateXPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCalibrateXPanel() {
		if (calibrateXPanel == null) {
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.gridx = 1;
			gridBagConstraints51.gridy = 4;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.gridy = 4;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.fill = GridBagConstraints.BOTH;
			gridBagConstraints31.gridy = 3;
			gridBagConstraints31.weightx = 1.0;
			gridBagConstraints31.weighty = 1.0;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridwidth = 1;
			gridBagConstraints1.gridy = 0;
			Headline = new JLabel();
			Headline.setText(Messages.getString("Acc3DCalibrationFrame.headline_calibration")); //$NON-NLS-1$
			Headline.setVerticalAlignment(SwingConstants.TOP);
			Headline.setVerticalTextPosition(SwingConstants.TOP);
			Headline.setFont(new Font("Dialog", Font.BOLD, 14));
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridy = 0;
			Icon = new JLabel();
			Icon.setText("");
			Icon.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Acc3DCalibrationFrame.file_calibration_normal")))); //$NON-NLS-1$
			calibrateXPanel = new JPanel();
			calibrateXPanel.setLayout(new GridBagLayout());
			calibrateXPanel.add(Icon, gridBagConstraints);
			calibrateXPanel.add(Headline, gridBagConstraints1);
			calibrateXPanel.add(getHowTo(), gridBagConstraints31);
			calibrateXPanel.add(getCancelButton(), gridBagConstraints41);
			calibrateXPanel.add(getNextButton(), gridBagConstraints51);
		}
		return calibrateXPanel;
	}

	/**
	 * This method initializes HowTo
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JTextPane getHowTo() {
		if (HowTo == null) {
			HowTo = new JTextPane();
			HowTo.setEditable(false);
			HowTo.setBackground(new Color(238, 238, 238));
			HowTo.setText(Messages.getString("Acc3DCalibrationFrame.desc_calibration_normal")); //$NON-NLS-1$
		}
		return HowTo;
	}

	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText(Messages.getString("Acc3DCalibrationFrame.button_cancel")); //$NON-NLS-1$
			cancelButton.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return cancelButton;
	}

	/**
	 * This method initializes nextButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNextButton() {
		if (nextButton == null) {
			nextButton = new JButton();
			nextButton.setText(Messages.getString("Acc3DCalibrationFrame.button_next")); //$NON-NLS-1$
			nextButton.setHorizontalAlignment(SwingConstants.RIGHT);
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					getCalibrateXPanel().setVisible(false);
					calibrationdata[0] = mote.doManualCalibrate();
					jTabbedPane.setSelectedComponent(getCalibrateYPanel());
					getCalibrateXPanel().setVisible(true);
				}
			});

		}
		return nextButton;
	}

	/**
	 * This method initializes calibrateButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCalibrateButton() {
		if (calibrateButton == null) {
			calibrateButton = new JButton();
			calibrateButton.setText(Messages.getString("Acc3DCalibrationFrame.button_calibrate")); //$NON-NLS-1$
			calibrateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					jTabbedPane.setSelectedComponent(getCalibrateXPanel());
				}
			});
		}
		return calibrateButton;
	}

	/**
	 * This method initializes calibrateYPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCalibrateYPanel() {
		if (calibrateYPanel == null) {
			GridBagConstraints gridBagConstraints511 = new GridBagConstraints();
			gridBagConstraints511.gridx = 1;
			gridBagConstraints511.gridy = 4;
			GridBagConstraints gridBagConstraints411 = new GridBagConstraints();
			gridBagConstraints411.gridx = 0;
			gridBagConstraints411.gridy = 4;
			GridBagConstraints gridBagConstraints311 = new GridBagConstraints();
			gridBagConstraints311.fill = GridBagConstraints.BOTH;
			gridBagConstraints311.gridx = 0;
			gridBagConstraints311.gridy = 3;
			gridBagConstraints311.weightx = 1.0;
			gridBagConstraints311.weighty = 1.0;
			gridBagConstraints311.gridwidth = 2;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridwidth = 1;
			gridBagConstraints15.gridy = 0;
			gridBagConstraints15.gridx = 0;
			Headline1 = new JLabel();
			Headline1.setFont(new Font("Dialog", Font.BOLD, 14));
			Headline1.setVerticalAlignment(SwingConstants.TOP);
			Headline1.setVerticalTextPosition(SwingConstants.TOP);
			Headline1.setText(Messages.getString("Acc3DCalibrationFrame.headline_calibration")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridwidth = 1;
			gridBagConstraints14.gridy = 0;
			gridBagConstraints14.gridx = 1;
			Icon1 = new JLabel();
			Icon1.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Acc3DCalibrationFrame.file_calibration_side")))); //$NON-NLS-1$
			Icon1.setText("");
			calibrateYPanel = new JPanel();
			calibrateYPanel.setLayout(new GridBagLayout());
			calibrateYPanel.add(Icon1, gridBagConstraints14);
			calibrateYPanel.add(Headline1, gridBagConstraints15);
			calibrateYPanel.add(getHowTo1(), gridBagConstraints311);
			calibrateYPanel.add(getCancelButton1(), gridBagConstraints411);
			calibrateYPanel.add(getNextButton1(), gridBagConstraints511);
		}
		return calibrateYPanel;
	}

	/**
	 * This method initializes HowTo1
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JTextPane getHowTo1() {
		if (HowTo1 == null) {
			HowTo1 = new JTextPane();
			HowTo1.setBackground(new Color(238, 238, 238));
			HowTo1.setEditable(false);
			HowTo1.setText(Messages.getString("Acc3DCalibrationFrame.desc_calibration_side")); //$NON-NLS-1$
		}
		return HowTo1;
	}

	/**
	 * This method initializes cancelButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton1() {
		if (cancelButton1 == null) {
			cancelButton1 = new JButton();
			cancelButton1.setHorizontalAlignment(SwingConstants.LEFT);
			cancelButton1.setText(Messages.getString("Acc3DCalibrationFrame.button_cancel")); //$NON-NLS-1$
		}
		return cancelButton1;
	}

	/**
	 * This method initializes nextButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNextButton1() {
		if (nextButton1 == null) {
			nextButton1 = new JButton();
			nextButton1.setHorizontalAlignment(SwingConstants.RIGHT);
			nextButton1.setText(Messages.getString("Acc3DCalibrationFrame.button_next")); //$NON-NLS-1$
			nextButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					getCalibrateYPanel().setVisible(false);
					calibrationdata[1] = mote.doManualCalibrate();
					jTabbedPane.setSelectedComponent(getCalibrateZPanel());
					getCalibrateYPanel().setVisible(true);
				}
			});

		}
		return nextButton1;
	}

	/**
	 * This method initializes calibrateZPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCalibrateZPanel() {
		if (calibrateZPanel == null) {
			GridBagConstraints gridBagConstraints5111 = new GridBagConstraints();
			gridBagConstraints5111.gridx = 1;
			gridBagConstraints5111.gridy = 4;
			GridBagConstraints gridBagConstraints4111 = new GridBagConstraints();
			gridBagConstraints4111.gridx = 0;
			gridBagConstraints4111.gridy = 4;
			GridBagConstraints gridBagConstraints3111 = new GridBagConstraints();
			gridBagConstraints3111.fill = GridBagConstraints.BOTH;
			gridBagConstraints3111.gridx = 0;
			gridBagConstraints3111.gridy = 3;
			gridBagConstraints3111.weightx = 1.0;
			gridBagConstraints3111.weighty = 1.0;
			gridBagConstraints3111.gridwidth = 2;
			GridBagConstraints gridBagConstraints151 = new GridBagConstraints();
			gridBagConstraints151.gridwidth = 1;
			gridBagConstraints151.gridy = 0;
			gridBagConstraints151.gridx = 0;
			Headline11 = new JLabel();
			Headline11.setFont(new Font("Dialog", Font.BOLD, 14));
			Headline11.setVerticalAlignment(SwingConstants.TOP);
			Headline11.setVerticalTextPosition(SwingConstants.TOP);
			Headline11.setText(Messages.getString("Acc3DCalibrationFrame.headline_calibration")); //$NON-NLS-1$
			GridBagConstraints gridBagConstraints141 = new GridBagConstraints();
			gridBagConstraints141.gridwidth = 1;
			gridBagConstraints141.gridy = 0;
			gridBagConstraints141.gridx = 1;
			Icon11 = new JLabel();
			Icon11.setIcon(new ImageIcon(getClass().getResource(Messages.getString("Acc3DCalibrationFrame.file_calibration_top")))); //$NON-NLS-1$
			Icon11.setText("");
			calibrateZPanel = new JPanel();
			calibrateZPanel.setLayout(new GridBagLayout());
			calibrateZPanel.add(Icon11, gridBagConstraints141);
			calibrateZPanel.add(Headline11, gridBagConstraints151);
			calibrateZPanel.add(getHowTo11(), gridBagConstraints3111);
			calibrateZPanel.add(getCancelButton11(), gridBagConstraints4111);
			calibrateZPanel.add(getNextButton11(), gridBagConstraints5111);
		}
		return calibrateZPanel;
	}

	/**
	 * This method initializes HowTo11
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JTextPane getHowTo11() {
		if (HowTo11 == null) {
			HowTo11 = new JTextPane();
			HowTo11.setBackground(new Color(238, 238, 238));
			HowTo11.setEditable(false);
			HowTo11.setText(Messages.getString("Acc3DCalibrationFrame.desc_calibration_top")); //$NON-NLS-1$
		}
		return HowTo11;
	}

	/**
	 * This method initializes cancelButton11
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton11() {
		if (cancelButton11 == null) {
			cancelButton11 = new JButton();
			cancelButton11.setHorizontalAlignment(SwingConstants.LEFT);
			cancelButton11.setText(Messages.getString("Acc3DCalibrationFrame.button_cancel")); //$NON-NLS-1$
		}
		return cancelButton11;
	}

	/**
	 * This method initializes nextButton11
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNextButton11() {
		if (nextButton11 == null) {
			nextButton11 = new JButton();
			nextButton11.setHorizontalAlignment(SwingConstants.RIGHT);
			nextButton11.setText(Messages.getString("Acc3DCalibrationFrame.button_next")); //$NON-NLS-1$
			nextButton11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					getCalibrateZPanel().setVisible(false);
					calibrationdata[2] = mote.doManualCalibrate();
					calculateCalibration();
					calculateConfig();
					getCalibrateZPanel().setVisible(true);
					jTabbedPane.setSelectedComponent(getStatusPanel());
				}
			});

		}
		return nextButton11;
	}

	private void calculateCalibration() {
		int zeroX = (calibrationdata[0].getMeanValue(0) + calibrationdata[2].getMeanValue(0)) / 2;
		int zeroY = (calibrationdata[0].getMeanValue(1) + calibrationdata[1].getMeanValue(1)) / 2;
		int zeroZ = (calibrationdata[1].getMeanValue(2) + calibrationdata[2].getMeanValue(2)) / 2;

		int gx = -Math.abs(calibrationdata[1].getMeanValue(0) - zeroX);
		int gy = -Math.abs(calibrationdata[2].getMeanValue(1) - zeroY);
		int gz = Math.abs(calibrationdata[0].getMeanValue(2) - zeroZ);

		mote
				.setCalibration(
						new int[] { zeroX, zeroY, zeroZ },
						new int[] { gx, gy, gz },
						new float[] { 9.81f, 9.81f, 9.81f },
						new String[] {
								Messages.getString("Acc3DCalibrationFrame.unit_m_per_s2"), Messages.getString("Acc3DCalibrationFrame.unit_m_per_s2"), Messages.getString("Acc3DCalibrationFrame.unit_m_per_s2") }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
