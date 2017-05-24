package org.phymote.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.phymote.PhyMote;
import org.phymote.control.PhyMoteDataFileFilter;
import org.phymote.control.exceptions.Messages;
import org.phymote.control.listener.MouseClickListener;
import org.phymote.gui.layout.ExtendedFlowLayout;
import org.phymote.gui.layout.PhyMoteDesktopManager;
import org.phymote.sensor.ChartModificationSensor;

/**
 * Main Program's Frame. Contains two parts: Left one to select sensors and
 * their functions, right one is an internalDesktop for Meters and Charts.
 * 
 * @author Christoph Krichenbauer
 * 
 */
public class PhyMoteFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel mainPane = null;
	private JPanel sensorPanel = null;
	private JDesktopPane mainDesktopContainer = null;

	private JLabel buttonAddMote = null;
	private JLabel buttonAddVirtualSensor = null;

	private JPanel panelMote = null;
	private JPanel panelVirtualSensor = null;

	private PhyMote rootPhyMote = null;

	private JLabel buttonRecord;

	private PhyMoteFrame myself = this;

	/**
	 * This is the default constructor
	 * 
	 * @param rootPhyMote
	 */
	public PhyMoteFrame(PhyMote myMainPhyMote) {
		super();
		rootPhyMote = myMainPhyMote;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setPreferredSize(new Dimension(800, 600));
		setName("mainGui");
		this.setBounds(new Rectangle(0, 0, 800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(getMainPane());
		setTitle(Messages.getString("PhyMoteFrame.window_title")); //$NON-NLS-1$
		setVisible(true);
		pack();
	}

	/**
	 * This method initializes mainPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPane() {
		if (mainPane == null) {
			mainPane = new JPanel();
			mainPane.setLayout(new BorderLayout());
			mainPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			mainPane.add(getSensorPanel(), BorderLayout.WEST);
			mainPane.add(getMainDesktopContainer(), BorderLayout.CENTER);
		}
		return mainPane;
	}

	/**
	 * This method initializes sensorPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSensorPanel() {
		if (sensorPanel == null) {
			final ImageIcon buttonWaiting = new ImageIcon(getClass().getResource(Messages.getString("PhyMoteFrame.file_icon_add_wii"))); //$NON-NLS-1$
			final ImageIcon buttonActive = new ImageIcon(getClass().getResource(Messages.getString("PhyMoteFrame.file_icon_add_wii_stop"))); //$NON-NLS-1$
			buttonAddMote = new JLabel();
			buttonAddMote.setText("");
			buttonAddMote.setIcon(buttonWaiting);
			buttonAddMote.setName("buttonAddMote");
			buttonAddMote.setToolTipText(Messages.getString("PhyMoteFrame.tooltip_connect")); //$NON-NLS-1$
			buttonAddMote.setPreferredSize(new Dimension(98, 34));
			buttonAddMote.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			buttonAddMote.addMouseListener(new MouseClickListener(buttonAddMote) {
				public void mousePressed(MouseEvent arg0) {
					rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
					buttonAddMote.setIcon(buttonActive);
					rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

					SensorConnectDialog sensorConnectDialog = new SensorConnectDialog(rootPhyMote.getMainFrame());

					if (sensorConnectDialog.hasResult()) {
						rootPhyMote.addSensor(sensorConnectDialog.getSensor());
					}

					buttonAddMote.setIcon(buttonWaiting);
				}

			});

			final ImageIcon buttonRecWaiting = new ImageIcon(getClass().getResource(Messages.getString("PhyMoteFrame.file_icon_rec"))); //$NON-NLS-1$
			final ImageIcon buttonRecActive = new ImageIcon(getClass().getResource(Messages.getString("PhyMoteFrame.file_icon_rec_searching"))); //$NON-NLS-1$

			buttonRecord = new JLabel();
			buttonRecord.setText("");
			buttonRecord.setIcon(buttonRecWaiting);
			buttonRecord.setToolTipText(Messages.getString("PhyMoteFrame.tooltip_start_stop_capture")); //$NON-NLS-1$
			buttonRecord.setName("buttonRecord"); //$NON-NLS-1$
			buttonRecord.setPreferredSize(new Dimension(98, 34));
			buttonRecord.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			buttonRecord.addMouseListener(new MouseClickListener(buttonRecord) {
				public void mousePressed(MouseEvent arg0) {
					if (buttonRecord.getIcon().equals(buttonRecWaiting)) {
						ReallySureDialog sureDialog = new ReallySureDialog(rootPhyMote.getMainFrame(), Messages.getString("PhyMoteFrame.StartCaptureMessage")); //$NON-NLS-1$
						if (sureDialog.getResult()) {
							rootPhyMote.startCapture();
							buttonRecord.setIcon(buttonRecActive);
						}
					} else {
						rootPhyMote.stopCapture();
						buttonRecord.setIcon(buttonRecWaiting);
					}
				};
			});

			buttonAddVirtualSensor = new JLabel();
			buttonAddVirtualSensor.setText("");
			buttonAddVirtualSensor.setIcon(new ImageIcon(getClass().getResource(Messages.getString("PhyMoteFrame.file_icon_add_virtual")))); //$NON-NLS-1$
			buttonAddVirtualSensor.setName("buttonAddVirtualSensor"); //$NON-NLS-1$
			buttonAddVirtualSensor.setPreferredSize(new Dimension(98, 34));
			buttonAddVirtualSensor.setBorder(BorderFactory.createLineBorder(Color.black, 1));

			buttonAddVirtualSensor.setToolTipText(Messages.getString("PhyMoteFrame.toolip_load")); //$NON-NLS-1$
			buttonAddVirtualSensor.addMouseListener(new MouseClickListener(buttonAddVirtualSensor) {
				public void mousePressed(MouseEvent arg0) {

					JFileChooser fc = new JFileChooser();
					fc.addChoosableFileFilter(new PhyMoteDataFileFilter());

					if (fc.showOpenDialog(myself) == JFileChooser.APPROVE_OPTION) {
						Cursor old_cursor = rootPhyMote.getMainFrame().getCursor();
						rootPhyMote.getMainFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
						File saveFile = fc.getSelectedFile();
						rootPhyMote.addVirtualSensor(new ChartModificationSensor(saveFile));
						rootPhyMote.getMainFrame().setCursor(old_cursor);
					}
				};
			});

			sensorPanel = new JPanel();
			sensorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 2));
			sensorPanel.setName("sensorPanel");
			sensorPanel.setBackground(Color.white);
			sensorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), Messages.getString("PhyMoteFrame.caption_sensors"), //$NON-NLS-1$
					TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, null, null));
			sensorPanel.setPreferredSize(new Dimension(104, 0));

			sensorPanel.add(buttonRecord, null);
			sensorPanel.add(buttonAddMote, null);
			sensorPanel.add(getPanelMote(), null);
			sensorPanel.add(buttonAddVirtualSensor, null);
			sensorPanel.add(getPanelVirtualSensor(), null);

		}
		return sensorPanel;
	}

	/**
	 * This method initializes mainDesktopContainer
	 * 
	 * @return javax.swing.JPanel
	 */
	public JDesktopPane getMainDesktopContainer() {
		if (mainDesktopContainer == null) {
			mainDesktopContainer = new JDesktopPane() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Component add(Component comp) {
					Component result = super.add(comp);
					result.setLocation(10, 20);
					return result;
				}
			};
			mainDesktopContainer.setBackground(Color.white);
			mainDesktopContainer.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
			mainDesktopContainer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			mainDesktopContainer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), Messages.getString("PhyMoteFrame.caption_data"), //$NON-NLS-1$
					TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, null, null));

			mainDesktopContainer.setDesktopManager(new PhyMoteDesktopManager(mainDesktopContainer));

		}
		return mainDesktopContainer;
	}

	/**
	 * This method initializes panelMote
	 * 
	 * @return javax.swing.JPanel
	 */
	public JPanel getPanelMote() {
		if (panelMote == null) {
			panelMote = new JPanel();
			panelMote.setLayout(new ExtendedFlowLayout(FlowLayout.CENTER, 0, 0));
			panelMote.setBackground(Color.white);
			panelMote.setName("panelMote");
			panelMote.setMaximumSize(new Dimension(98, 392));
		}
		return panelMote;
	}

	public JPanel getPanelVirtualSensor() {
		if (panelVirtualSensor == null) {
			panelVirtualSensor = new JPanel();
			panelVirtualSensor.setLayout(new ExtendedFlowLayout(FlowLayout.CENTER, 0, 0));
			panelVirtualSensor.setName("panelVirtualSensor");
			panelVirtualSensor.setMaximumSize(new Dimension(98, 392));
			panelVirtualSensor.setBackground(Color.white);
		}
		return panelVirtualSensor;
	}

}
