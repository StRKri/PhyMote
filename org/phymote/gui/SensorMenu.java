package org.phymote.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.phymote.PhyMote;
import org.phymote.control.exceptions.Messages;
import org.phymote.control.listener.MouseClickListener;
import org.phymote.sensor.Acc3DSensor;

public class SensorMenu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel ButtonMeterX = null;
	private JLabel ButtonMeterY = null;
	private JLabel ButtonMeterZ = null;
	private JLabel MoteCounter = null;
	private JLabel ButtonTrash = null;
	private JLabel ButtonSettings = null;
	private JLabel ButtonChart = null;

	private PhyMote rootPhyMote;
	private Acc3DSensor mote;
	private int counter;

	/**
	 * This is the default constructor
	 */
	public SensorMenu(PhyMote myMainPhyMote, Acc3DSensor myMote, int myCount) {
		super();
		counter = myCount;
		mote = myMote;
		rootPhyMote = myMainPhyMote;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.gridx = 2;
		gridBagConstraints6.gridy = 0;
		ButtonChart = new JLabel();
		ButtonChart.setText("");
		ButtonChart.setIcon(new ImageIcon(getClass().getResource(Messages.getString("SensorMenu.file_icon_chart")))); //$NON-NLS-1$
		ButtonChart.setBackground(Color.white);
		ButtonChart.setName("ButtonChart");
		ButtonChart.addMouseListener(new MouseClickListener(ButtonChart) {
			public void mousePressed(MouseEvent arg0) {
				Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
				rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				rootPhyMote.addChartFrame(mote);
				rootPhyMote.getMainFrame().setCursor(old_cursor);
			}

		});
		ButtonChart.setToolTipText(Messages.getString("SensorMenu.tooltip_showchart")); //$NON-NLS-1$

		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 2;
		gridBagConstraints5.gridy = 1;
		ButtonSettings = new JLabel();
		ButtonSettings.setText("");
		ButtonSettings.setIcon(new ImageIcon(getClass().getResource(Messages.getString("SensorMenu.file_icon_preferences")))); //$NON-NLS-1$
		ButtonSettings.setBackground(Color.white);
		ButtonSettings.setName("ButtonSettings");
		ButtonSettings.addMouseListener(new MouseClickListener(ButtonSettings) {
			public void mousePressed(MouseEvent arg0) {
				Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
				rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				rootPhyMote.addSensorCalibrationFrame(mote);
				rootPhyMote.getMainFrame().setCursor(old_cursor);
			}
		});
		ButtonSettings.setToolTipText(Messages.getString("SensorMenu.tooltip_calibrate")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 2;
		gridBagConstraints4.gridy = 2;
		ButtonTrash = new JLabel();
		ButtonTrash.setText("");
		ButtonTrash.setIcon(new ImageIcon(getClass().getResource(Messages.getString("SensorMenu.file_icon_trash")))); //$NON-NLS-1$
		ButtonTrash.setBackground(Color.white);
		ButtonTrash.setName("ButtonTrash");
		ButtonTrash.addMouseListener(new MouseClickListener(this) {
			public void mousePressed(MouseEvent arg0) {
				Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
				rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				rootPhyMote.removeSensor(menu);
				rootPhyMote.getMainFrame().setCursor(old_cursor);
			}
		});
		ButtonTrash.setToolTipText(Messages.getString("SensorMenu.tooltip_dispose")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridheight = 3;
		gridBagConstraints3.gridy = 0;
		MoteCounter = new JLabel();
		MoteCounter.setText(""); //$NON-NLS-1$
		MoteCounter.setIcon(new ImageIcon(getClass().getResource(
				Messages.getString("SensorMenu.file_icon_wii_countable") + counter + Messages.getString("SensorMenu.file_icon_wii_filetype")))); //$NON-NLS-1$ //$NON-NLS-2$
		MoteCounter.setBackground(Color.white);
		MoteCounter.setName("MoteCounter");

		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.gridy = 2;
		ButtonMeterZ = new JLabel();
		ButtonMeterZ.setText("");
		ButtonMeterZ.setIcon(new ImageIcon(getClass().getResource(Messages.getString("SensorMenu.file_icon_meter_z")))); //$NON-NLS-1$
		ButtonMeterZ.setBackground(Color.white);
		ButtonMeterZ.setName("ButtonMeterZ");
		ButtonMeterZ.addMouseListener(new MouseClickListener(ButtonMeterZ) {
			public void mousePressed(MouseEvent arg0) {
				Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
				rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				rootPhyMote.addMeterFrame(mote, 2);
				rootPhyMote.getMainFrame().setCursor(old_cursor);
			}
		});
		ButtonMeterZ.setToolTipText(Messages.getString("SensorMenu.tooltip_show_meter")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 1;
		ButtonMeterY = new JLabel();
		ButtonMeterY.setText("");
		ButtonMeterY.setName("ButtonMeterY");
		ButtonMeterY.setIcon(new ImageIcon(getClass().getResource(Messages.getString("SensorMenu.file_icon_meter_y")))); //$NON-NLS-1$
		ButtonMeterY.setBackground(Color.white);
		ButtonMeterY.addMouseListener(new MouseClickListener(ButtonMeterY) {
			public void mousePressed(MouseEvent arg0) {
				Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
				rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				rootPhyMote.addMeterFrame(mote, 1);
				rootPhyMote.getMainFrame().setCursor(old_cursor);
			}
		});
		ButtonMeterY.setToolTipText(Messages.getString("SensorMenu.tooltip_show_meter")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		ButtonMeterX = new JLabel();
		ButtonMeterX.setText("");
		ButtonMeterX.setIcon(new ImageIcon(getClass().getResource(Messages.getString("SensorMenu.file_icon_meter_x")))); //$NON-NLS-1$
		ButtonMeterX.setBackground(Color.white);
		ButtonMeterX.setName("ButtonMeterX");
		ButtonMeterX.addMouseListener(new MouseClickListener(ButtonMeterX) {
			public void mousePressed(MouseEvent arg0) {
				Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
				rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				rootPhyMote.addMeterFrame(mote, 0);
				rootPhyMote.getMainFrame().setCursor(old_cursor);
			}
		});
		ButtonMeterX.setToolTipText(Messages.getString("SensorMenu.tooltip_show_meter")); //$NON-NLS-1$

		setLayout(new GridBagLayout());
		this.setBounds(new Rectangle(0, 0, 98, 98));
		this.setSize(98, 98);
		setName("MoteMenu");
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.add(ButtonMeterX, gridBagConstraints);
		this.add(ButtonMeterY, gridBagConstraints1);
		this.add(ButtonMeterZ, gridBagConstraints2);
		this.add(MoteCounter, gridBagConstraints3);
		this.add(ButtonTrash, gridBagConstraints4);
		this.add(ButtonSettings, gridBagConstraints5);
		this.add(ButtonChart, gridBagConstraints6);
	}

	public Acc3DSensor getMote() {
		return mote;
	}

}
