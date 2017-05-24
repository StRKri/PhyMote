package org.phymote.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import motej.MoteFinder;

import org.phymote.control.exceptions.Messages;
import org.phymote.mote.listeners.MoteFoundListener;
import org.phymote.sensor.Acc3DSensor;
import org.phymote.sensor.PhSensor;

public class SensorConnectDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTextArea aTextArea;
	private JButton connectButton;
	private Acc3DSensor mySensor;
	private JButton retryButton;
	private JScrollPane jScrollPane1;
	private boolean connecting = false;
	private SensorConnectDialog mySelf = this;

	private class ShowDiscoveryListener implements DiscoveryListener {
		private SensorConnectDialog dialog;

		public ShowDiscoveryListener(SensorConnectDialog myDialog) {
			dialog = myDialog;
		}

		public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
			dialog.aTextArea.append(Messages.getString("SensorConnectDialog.discoveredDevice")); //$NON-NLS-1$

		}

		public void inquiryCompleted(int discType) {
			if (discType == DiscoveryListener.INQUIRY_COMPLETED) {
				dialog.aTextArea.append(Messages.getString("SensorConnectDialog.inquiryCompleted")); //$NON-NLS-1$
			}

			if (discType == DiscoveryListener.INQUIRY_TERMINATED) {
				dialog.aTextArea.append(Messages.getString("SensorConnectDialog.inquiryTerminated")); //$NON-NLS-1$
			}

			if (discType == DiscoveryListener.INQUIRY_ERROR) {
				dialog.aTextArea.append(Messages.getString("SensorConnectDialog.inquiryError")); //$NON-NLS-1$
			}
		}

		public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
		}

		public void serviceSearchCompleted(int arg0, int arg1) {
		}
	}

	private DiscoveryListener listener;

	public SensorConnectDialog(JFrame frame) {
		super(frame, true);
		setLocationRelativeTo(frame);
		initGUI();

		try {
			listener = new ShowDiscoveryListener(this);
			MoteFinder.getMoteFinder().addDiscoveryListener(listener);
		} catch (RuntimeException rex) {
			if (rex.getCause().getClass().equals(BluetoothStateException.class)) {
				aTextArea.append(Messages.getString("SensorConnectDialog.DeviceNotFound"));
			}
		}

		pack();
		this.setSize(367, 356);
		setVisible(true);
	}

	private void initGUI() {
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] { 0.1, 0.1 };
		thisLayout.rowHeights = new int[] { 7, 7 };
		thisLayout.columnWeights = new double[] { 0.1, 0.1 };
		thisLayout.columnWidths = new int[] { 7, 7 };
		getContentPane().setLayout(thisLayout);

		connectButton = new JButton();
		getContentPane().add(connectButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		connectButton.setText(Messages.getString("SensorConnectDialog.ConnectButton")); //$NON-NLS-1$
		retryButton = new JButton();
		getContentPane().add(retryButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		retryButton.setText(Messages.getString("SensorConnectDialog.RetryButton")); //$NON-NLS-1$
		retryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!connecting) {

				} else {
					Acc3DSensor.stopMoteDiscovery();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					aTextArea.append(Messages.getString("SensorConnectDialog.Restarting")); //$NON-NLS-1$
					Acc3DSensor.startMoteDiscovery(new MoteFoundListener() {
						public void moteFound(Acc3DSensor myMote) {
							mySensor = myMote;
							mySelf.close();
						}
					});
				}
			}
		});
		jScrollPane1 = new JScrollPane();
		getContentPane().add(jScrollPane1, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jScrollPane1.setWheelScrollingEnabled(true);
		jScrollPane1.setPreferredSize(new java.awt.Dimension(356, 269));
		{
			aTextArea = new JTextArea(6, 20);
			jScrollPane1.setViewportView(aTextArea);
			aTextArea.setEditable(false);
			aTextArea.setText(Messages.getString("SensorConnectDialog.PressOneAndTwo")); //$NON-NLS-1$
			aTextArea.setPreferredSize(new java.awt.Dimension(-1, -1));
			aTextArea.setLineWrap(true);
			aTextArea.setSize(-1, -1);
			aTextArea.setRows(200);
		}
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!connecting) {
					connecting = true;
					connectButton.setText(Messages.getString("SensorConnectDialog.CancelButton")); //$NON-NLS-1$
					aTextArea.append("\n"); //$NON-NLS-1$
					Acc3DSensor.startMoteDiscovery(new MoteFoundListener() {
						public void moteFound(Acc3DSensor myMote) {
							mySensor = myMote;
							mySelf.close();
						}
					});
					retryButton.setEnabled(true);
				} else {
					Acc3DSensor.stopMoteDiscovery();
					retryButton.setEnabled(false);
					mySelf.close();
				}
			}
		});
	}

	protected void close() {
		try {
			MoteFinder.getMoteFinder().removeDiscoveryListener(listener);
		} catch (Exception e) {
			// ignore!!
		}
		mySelf.dispose();
	}

	class FilteredStream extends FilterOutputStream {
		public FilteredStream(OutputStream aStream) {
			super(aStream);
		}

		@Override
		public void write(byte b[]) throws IOException {
			String aString = new String(b);
			aTextArea.append(aString);
		}

		@Override
		public void write(byte b[], int off, int len) throws IOException {
			String aString = new String(b, off, len);
			aTextArea.append(aString);
		}
	}

	public PhSensor getSensor() {
		return mySensor;
	}

	public boolean hasResult() {
		return (mySensor != null);
	}

}
