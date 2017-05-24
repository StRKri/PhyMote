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
import org.phymote.sensor.ChartModificationSensor;

public class VirtualSensorMenu extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel MoteCounter = null;
	private JLabel ButtonTrash = null;
	private JLabel ButtonChart = null;

	private PhyMote rootPhyMote;
	private ChartModificationSensor mote;
	private int counter;

	private VirtualSensorMenu mySelf = this;

	/**
	 * This is the default constructor
	 */
	public VirtualSensorMenu(PhyMote myMainPhyMote, ChartModificationSensor myMote, int myCount) {
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
		gridBagConstraints6.gridx = 1;
		gridBagConstraints6.gridy = 0;
		ButtonChart = new JLabel();
		ButtonChart.setText("");
		ButtonChart.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorMenu.file_icon_chart")))); //$NON-NLS-1$
		ButtonChart.setBackground(Color.white);
		ButtonChart.setName("ButtonChart");
		ButtonChart.addMouseListener(new MouseClickListener(ButtonChart) {
			public void mousePressed(MouseEvent e) {
				Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
				rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				rootPhyMote.addVirtualChartFrame(mote);
				rootPhyMote.getMainFrame().setCursor(old_cursor);
			}
		});
		ButtonChart.setToolTipText(Messages.getString("VirtualSensorMenu.tooltip_show_chart")); //$NON-NLS-1$
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 2;
		gridBagConstraints4.gridy = 0;
		ButtonTrash = new JLabel();
		ButtonTrash.setText("");
		ButtonTrash.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorMenu.file_icon_trash")))); //$NON-NLS-1$
		ButtonTrash.setBackground(Color.white);
		ButtonTrash.setName("ButtonTrash");
		ButtonTrash.addMouseListener(new MouseClickListener(ButtonTrash) {
			public void mousePressed(MouseEvent arg0) {
				Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
				PhyMote.getRootPhyMote().getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
				PhyMote.getRootPhyMote().removeVirtualSensor(mySelf);
				PhyMote.getRootPhyMote().getMainFrame().setCursor(old_cursor);
			}
		});
		ButtonTrash.setToolTipText(Messages.getString("VirtualSensorMenu.tooltip_dispose")); //$NON-NLS-1$

		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.gridy = 0;
		MoteCounter = new JLabel();
		MoteCounter.setText("");
		MoteCounter.setIcon(new ImageIcon(getClass().getResource(
				Messages.getString("VirtualSensorMenu.file_icon_virtual_countable") + counter + Messages.getString("VirtualSensorMenu.file_icon_virtual_filetype")))); //$NON-NLS-1$ //$NON-NLS-2$
		MoteCounter.setBackground(Color.white);
		MoteCounter.setName("MoteCounter");

		setLayout(new GridBagLayout());
		this.setBounds(new Rectangle(0, 0, 98, 34));
		this.setSize(98, 34);
		setName("MoteMenu");
		setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.add(ButtonTrash, gridBagConstraints4);
		this.add(MoteCounter, gridBagConstraints3);
		this.add(ButtonChart, gridBagConstraints6);
	}

	public ChartModificationSensor getSensor() {
		return mote;
	}

}
