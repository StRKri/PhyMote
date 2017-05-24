package org.phymote;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.data.Range;
import org.phymote.control.listener.LinkedZoomDomainListener;
import org.phymote.control.listener.LinkedZoomValueListener;
import org.phymote.gui.Acc3DCalibrationFrame;
import org.phymote.gui.ChartInternalFrame;
import org.phymote.gui.MeterInternalFrame;
import org.phymote.gui.PhyMoteFrame;
import org.phymote.gui.SensorMenu;
import org.phymote.gui.VirtualSensorChartInternalFrame;
import org.phymote.gui.VirtualSensorMenu;
import org.phymote.persistance.Settings;
import org.phymote.sensor.Acc3DSensor;
import org.phymote.sensor.ChartModificationSensor;
import org.phymote.sensor.PhSensor;

public class PhyMote {
	/**
	 * Main Phymote Class
	 * 
	 * @author Christoph Krichenbauer
	 */
	private PhyMoteFrame mainFrame;
	private HashSet<PhSensor> sensors;
	private HashSet<SensorMenu> sensorMenus;
	private HashSet<ChartInternalFrame> frames;
	private Timer secondsTimer;
	private boolean capture;
	private Settings settings;
	private LinkedZoomDomainListener linkedZoomDomainListener;
	private LinkedZoomValueListener linkedZoomValueListener;

	/**
	 * Main Constructor. Starts the Program.
	 * 
	 */
	public PhyMote() {
		try {
			System.setProperty("bluecove.jsr82.psm_minimum_off", "true");
		} catch (Throwable e) {
			// do nothing...
		}

		mainFrame = new PhyMoteFrame(this);
		mainFrame.pack();
		mainFrame.setVisible(true);

		sensors = new HashSet<PhSensor>();
		sensorMenus = new HashSet<SensorMenu>();
		frames = new HashSet<ChartInternalFrame>();

		// generate or open settings-xml file.
		settings = new Settings();

		// generate LinkedZoomLister for all ChartInternalFrames;
		linkedZoomDomainListener = new LinkedZoomDomainListener(this);
		linkedZoomValueListener = new LinkedZoomValueListener(this);

	}

	/**
	 * Program is strongly aggregated with main JFrame.
	 * 
	 * @return Main Frame
	 */
	public PhyMoteFrame getMainFrame() {
		return mainFrame;
	}

	/**
	 * Add a real Sensor to running PhyMote Program and GUI
	 * 
	 * @param sensor
	 *            3D-Acceleration sensor to be added.
	 */
	public void addSensor(PhSensor sensor) {
		SensorMenu myMenu = new SensorMenu(this, (Acc3DSensor) sensor, sensor.getCounter());

		sensors.add(sensor);
		sensorMenus.add(myMenu);

		sensor.setNewDefaultDataRow();
		mainFrame.getPanelMote().add(myMenu, null);
		mainFrame.getPanelMote().updateUI();

	}

	/**
	 * Add a virtual Sensor to running PhyMote Program and GUI
	 * 
	 * @param sensor
	 *            virtual sensor to be added
	 */
	public void addVirtualSensor(ChartModificationSensor sensor) {
		sensors.add(sensor);
		mainFrame.getPanelVirtualSensor().add(new VirtualSensorMenu(this, sensor, sensor.getCounter()), null);
		mainFrame.getPanelVirtualSensor().updateUI();

	}

	/**
	 * Removes a Real sensor from Program and GUI (if available)
	 * 
	 * @param sensor
	 *            3D-Acceleration sensor to be removed
	 */
	public void removeSensor(PhSensor sensor) {
		sensors.remove(sensor);

		for (SensorMenu menu : sensorMenus) {
			if (menu.getMote().equals(sensor)) {
				mainFrame.getPanelMote().remove(menu);
				mainFrame.getPanelMote().updateUI();
			}
		}

		sensor.disconnect();
	}

	/**
	 * Removes a Real sensor from Program and GUI
	 * 
	 * @param menu
	 *            SensorMenu to be removed
	 */
	public void removeSensor(SensorMenu menu) {
		removeSensor(menu.getMote());
		mainFrame.getPanelMote().remove(menu);
		mainFrame.getPanelMote().updateUI();
	}

	/**
	 * Removes a Virtual sensor from Program and GUI
	 * 
	 * @param menu
	 *            VirtualSensorMenu to be removed
	 */
	public void removeVirtualSensor(VirtualSensorMenu menu) {
		removeSensor(menu.getSensor());
		ChartModificationSensor.releaseCounter(menu.getSensor().getCounter());
		mainFrame.getPanelVirtualSensor().remove(menu);
		mainFrame.getPanelVirtualSensor().updateUI();
	}

	/**
	 * Get all used real Sensors.
	 * 
	 * @return HashSet of active Sensors
	 */
	public HashSet<PhSensor> getSensors() {
		return sensors;
	}

	/**
	 * Start Capturing Data on all available Sensors.
	 */
	public void startCapture() {
		for (PhSensor sensor : sensors) {
			sensor.setNewDefaultDataRow();
			sensor.captureData();
		}
		secondsTimer = new Timer();
		secondsTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (isCaptureing()) {
					for (ChartInternalFrame frame : frames) {
						frame.updateZoom();
					}
				}
			}
		}, 100, 5000);

		// inform all frames that DataRow has Changed.
		for (ChartInternalFrame frame : frames) {
			frame.informNewDataRow();
		}

		capture = true;
	}

	/**
	 * End Capturing Data on all available Sensors.
	 */
	public void stopCapture() {
		for (PhSensor sensor : sensors) {
			sensor.stopCaptureData();
			sensor.getActiveDataRow().updateChart();
		}
		secondsTimer.cancel();
		capture = false;
	}

	/**
	 * Open a new ChartFame in Main GUI for real sensor.
	 * 
	 * @param mySensor
	 *            sensor to open chart for.
	 */
	public void addChartFrame(PhSensor mySensor) {
		if (!sensors.contains(mySensor)) {
			addSensor(mySensor);
		}
		ChartInternalFrame myChartFrame = new ChartInternalFrame(this, mySensor.getName(), mySensor);
		mainFrame.getMainDesktopContainer().add(myChartFrame);
		frames.add(myChartFrame);
		myChartFrame.moveToFront();
		myChartFrame.requestFocus();
		myChartFrame.updateZoom();
	}

	/**
	 * Update Zoom on all ChartFrames marked as linked
	 * 
	 * @param range
	 *            DomainAxis Zoom Range
	 */
	public void linkChartFramesDomainZoom(Range range) {
		for (ChartInternalFrame frame : frames) {
			frame.updateDomainZoom(range);
		}
	}

	/**
	 * Update Zoom on all ChartFrames marked as linked
	 * 
	 * @param range
	 *            ValueAxis Zoom Range
	 */
	public void linkChartFramesValueZoom(Range range) {
		for (ChartInternalFrame frame : frames) {
			frame.updateValueZoom(range);
		}
	}

	/**
	 * Open a new ChartModificationFrame in Main GUI for virtual Sensor.
	 * 
	 * @param mySensor
	 *            virtual sensor to open chart for.
	 */
	public void addVirtualChartFrame(ChartModificationSensor mySensor) {
		if (!sensors.contains(mySensor)) {
			addVirtualSensor(mySensor);
		}
		ChartInternalFrame myChartFrame = new VirtualSensorChartInternalFrame(this, mySensor.getName(), mySensor);

		mainFrame.getMainDesktopContainer().add(myChartFrame);
		frames.add(myChartFrame);
		myChartFrame.moveToFront();
		myChartFrame.requestFocus();
		myChartFrame.updateZoom();
	}

	/**
	 * Open new Calibration/Settings Frame for real sensor in Main GUI
	 * 
	 * @param mySensor
	 *            sensor to open calibration for.
	 */
	public void addSensorCalibrationFrame(Acc3DSensor mySensor) {
		if (!sensors.contains(mySensor)) {
			addSensor(mySensor);
		}
		Acc3DCalibrationFrame myCalibrationFrame = new Acc3DCalibrationFrame(mySensor);
		mainFrame.getMainDesktopContainer().add(myCalibrationFrame);
		myCalibrationFrame.moveToFront();
		myCalibrationFrame.requestFocus();
	}

	/**
	 * Removes a ChartFrame from Main GUI (i.e closes the window)
	 * 
	 * @param myChartFrame
	 *            ChartFrame to be closed
	 */
	public void removeChartFrame(ChartInternalFrame myChartFrame) {
		frames.remove(myChartFrame);
	}

	/**
	 * Open new Meter for real sensor in Main GUI
	 * 
	 * @param mySensor
	 *            sensor to open Meter for
	 * @param myDimension
	 *            dimension (0-3)
	 */
	public void addMeterFrame(PhSensor mySensor, int myDimension) {
		if (!sensors.contains(mySensor)) {
			addSensor(mySensor);
		}

		MeterInternalFrame myMeterFrame = new MeterInternalFrame(mySensor.getName() + " " + mySensor.getDesc(myDimension) + " [" + mySensor.getUnit(myDimension) + "]", mySensor
				.getCalibratedValueChart(myDimension));
		mainFrame.getMainDesktopContainer().add(myMeterFrame);
		myMeterFrame.requestFocus();
		myMeterFrame.moveToFront();
	}

	/**
	 * Get live capturing status
	 * 
	 * @return true, if capturing right now. else false.
	 */
	public boolean isCaptureing() {
		return capture;
	}

	/**
	 * Generate the standard listener for linked zooming
	 * 
	 * @return listener for Domain Axis
	 */
	public LinkedZoomDomainListener getLinkedZoomDomainListener() {
		return linkedZoomDomainListener;
	}

	/**
	 * Generate the standard listener for linked zooming
	 * 
	 * @return listener for Value Axis
	 */
	public LinkedZoomValueListener getLinkedZoomValueListener() {
		return linkedZoomValueListener;
	}

	/**
	 * Fetch the standard settings-xml object.
	 * 
	 * @return settings
	 */
	public Settings getSettings() {
		return settings;
	}

	private static final PhyMote rootPhyMote = new PhyMote();

	public static PhyMote getRootPhyMote() {
		return PhyMote.rootPhyMote;
	}

	/**
	 * Start program
	 * 
	 * @param args
	 *            no arguments working
	 */
	public static void main(String[] args) {
		// new PhyMote(); (done by static final)
	}

}
