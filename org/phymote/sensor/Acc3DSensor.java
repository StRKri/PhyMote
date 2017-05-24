package org.phymote.sensor;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.BluetoothStateException;

import motej.Mote;
import motej.MoteFinder;
import motej.MoteFinderListener;
import motej.event.AccelerometerEvent;
import motej.event.AccelerometerListener;
import motej.event.CoreButtonListener;
import motej.event.MoteDisconnectedEvent;
import motej.event.MoteDisconnectedListener;
import motej.request.ReportModeRequest;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.phymote.PhyMote;
import org.phymote.control.exceptions.Messages;
import org.phymote.mote.listeners.MoteFoundListener;
import org.phymote.sensor.calibration.Acc3DCalibrationData;
import org.phymote.sensor.data.Acc3DData;
import org.phymote.sensor.datarow.Acc3DDataRow;
import org.phymote.sensor.datarow.PhDataRow;

public class Acc3DSensor extends NameablePhSensor implements PhSensor {
	private final int dimensions = 3;

	private Acc3DCalibrationData calibration;
	private DefaultValueDataset[] valueDatasets;
	private JFreeChart[] valueCharts;
	private Mote mote;
	private PhDataRow activeDataRow;
	private int counter;
	private int meterCounter;
	private Acc3DSensor mySelf = this;

	public Acc3DSensor() {
		calibration = new Acc3DCalibrationData();

		initValueCharts();

		// increase Count.
		Acc3DSensor.moteCounter++;
		counter = Acc3DSensor.moteCounter;
		setName("Mote " + getCounter());
		setDesc(new String[] { "a_x", "a_y", "a_z" });
	}

	public Acc3DSensor(String myName) {
		setName(myName);
	}

	private void initValueCharts() {
		meterCounter = 0;
		valueDatasets = new DefaultValueDataset[] { new DefaultValueDataset(), new DefaultValueDataset(), new DefaultValueDataset() };

		MeterPlot plotx = new MeterPlot(valueDatasets[0]);
		MeterPlot ploty = new MeterPlot(valueDatasets[1]);
		MeterPlot plotz = new MeterPlot(valueDatasets[2]);

		plotx.setRange(new Range(-20.0f, 20.0f));
		ploty.setRange(new Range(-20.0f, 20.0f));
		plotz.setRange(new Range(-20.0f, 20.0f));

		plotx.setMeterAngle(120);
		ploty.setMeterAngle(120);
		plotz.setMeterAngle(120);

		plotx.setNeedlePaint(Color.BLACK);
		ploty.setNeedlePaint(Color.BLACK);
		plotz.setNeedlePaint(Color.BLACK);

		plotx.setTickSize(10.0);
		ploty.setTickSize(10.0);
		plotz.setTickSize(10.0);

		plotx.setDialShape(DialShape.PIE);
		ploty.setDialShape(DialShape.PIE);
		plotz.setDialShape(DialShape.PIE);

		plotx.setDialBackgroundPaint(Color.WHITE);
		ploty.setDialBackgroundPaint(Color.WHITE);
		plotz.setDialBackgroundPaint(Color.WHITE);

		plotx.setValuePaint(Color.BLACK);
		ploty.setValuePaint(Color.BLACK);
		plotz.setValuePaint(Color.BLACK);

		plotx.setTickPaint(Color.GRAY);
		ploty.setTickPaint(Color.GRAY);
		plotz.setTickPaint(Color.GRAY);

		plotx.setTickLabelFormat(NumberFormat.getNumberInstance());
		ploty.setTickLabelFormat(NumberFormat.getNumberInstance());
		plotz.setTickLabelFormat(NumberFormat.getNumberInstance());

		plotx.setTickLabelsVisible(true);
		ploty.setTickLabelsVisible(true);
		plotz.setTickLabelsVisible(true);

		plotx.setTickLabelPaint(Color.GRAY);
		ploty.setTickLabelPaint(Color.GRAY);
		plotz.setTickLabelPaint(Color.GRAY);

		plotx.setUnits(getUnit(0));
		ploty.setUnits(getUnit(1));
		plotz.setUnits(getUnit(2));

		valueCharts = new JFreeChart[] { new JFreeChart("a_x", JFreeChart.DEFAULT_TITLE_FONT, plotx, false), new JFreeChart("a_y", JFreeChart.DEFAULT_TITLE_FONT, ploty, false),
				new JFreeChart("a_z", JFreeChart.DEFAULT_TITLE_FONT, plotz, false), };
	}

	public Acc3DSensor connect() {
		MoteFinder moteFinder = MoteFinder.getMoteFinder();
		moteFinder.addMoteFinderListener(new MoteFinderListener() {
			@SuppressWarnings("unchecked")
			public void moteFound(Mote myMote) {
				mote = myMote;
				mote.addAccelerometerListener(accListen);
				mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
				boolean leds[] = new boolean[] { false, false, false, false };
				leds[getCounter() - 1] = true;
				mote.setPlayerLeds(leds);
				mote.rumble(1000);
				calibration.loadFromConfig(mote.getBluetoothAddress());
			}
		});

		moteFinder.startDiscovery();
		try {
			Thread.sleep(30000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		moteFinder.stopDiscovery();
		return this;
	}

	@SuppressWarnings("unchecked")
	public Acc3DSensor connect(Mote myMote) {
		mote = myMote;
		mote.addAccelerometerListener(accListen);
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
		boolean leds[] = new boolean[] { false, false, false, false };
		leds[getCounter() - 1] = true;
		mote.setPlayerLeds(leds);
		mote.rumble(1000);
		calibration.loadFromConfig(mote.getBluetoothAddress());

		mote.addMoteDisconnectedListener(new MoteDisconnectedListener<Mote>() {
			public void moteDisconnected(MoteDisconnectedEvent<Mote> arg0) {
				mote.disconnect();
				PhyMote.getRootPhyMote().removeSensor(mySelf);
				System.err.println("Mote " + mySelf.getCounter() + " disconnected");
			}
		});
		return this;
	}

	public void disconnect() {
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
		mote.disconnect();
	}

	public int getDimension() {
		return dimensions;
	}

	public String getUnit(int myDimension) {
		return calibration.getUnit(myDimension);
	}

	public void setCalibration(int[] zeros, int[] valueInts, float[] valueFloats, String[] myUnits) {
		calibration.setCalibration(zeros, valueInts, valueFloats, myUnits);
	}

	public float getCalibratedValue(int myDimension, int value) {
		return calibration.getCalibratedValue(myDimension, value);
	}

	public float getCalibratedValue(int myDimension, double value) {
		return calibration.getCalibratedValue(myDimension, value);
	}

	public Float getCalibrateRepresentValue(int myDimension) {
		return calibration.getCalibrateRepresentValue(myDimension);
	}

	public PhDataRow getActiveDataRow() {
		return activeDataRow;
	}

	public void setActiveDataRow(PhDataRow myData) {
		activeDataRow = myData;
	}

	public void setNewDefaultDataRow() {
		setActiveDataRow(new Acc3DDataRow(this));
		// setActiveDataRow(new Acc1DData(this));
	}

	@SuppressWarnings("unchecked")
	private AccelerometerListener accListen = new AccelerometerListener() {
		public void accelerometerChanged(AccelerometerEvent evt) {
			try {
				Acc3DData eventData = new Acc3DData(activeDataRow, evt.getX(), evt.getY(), evt.getZ());
				activeDataRow.addData(eventData);
				updateMeters(evt);
			} catch (NullPointerException e) {
				// ignore!
			}
		}

	};

	@SuppressWarnings("unchecked")
	private void updateMeters(AccelerometerEvent evt) {
		if (meterCounter > 9) {
			valueDatasets[0].setValue(getCalibratedValue(0, evt.getX()));
			valueDatasets[1].setValue(getCalibratedValue(1, evt.getY()));
			valueDatasets[2].setValue(getCalibratedValue(2, evt.getZ()));
			meterCounter = 0;
		} else {
			meterCounter++;
		}
	}

	public void captureData() {
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x31);
	}

	public void stopCaptureData() {
		mote.setReportMode(ReportModeRequest.DATA_REPORT_0x30);
		getActiveDataRow().updateChart();
		valueDatasets[0].setValue(null);
		valueDatasets[1].setValue(null);
		valueDatasets[2].setValue(null);
	}

	public Mote getMote() {
		return mote;
	}

	public void addButtonListener(CoreButtonListener cbl) {
		mote.addCoreButtonListener(cbl);
	}

	public Acc3DDataRow doManualCalibrate() {
		stopCaptureData();

		Acc3DDataRow plain = new Acc3DDataRow(this);
		setActiveDataRow(plain);
		captureData();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		stopCaptureData();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		return plain;
	}

	public ValueDataset getCalibratedValue(int myDimension) {
		return valueDatasets[myDimension];
	}

	public JFreeChart getCalibratedValueChart(int myDimension) {
		return valueCharts[myDimension];
	}

	public boolean isVirtualSensor() {
		return false;
	}

	public int getCalibrationZero(int myDimension) {
		return calibration.getZeros(myDimension);
	}

	public int getCalibrationValue(int myDimension) {
		return getCalibrationGravity(myDimension);
	}

	public int getCalibrationGravity(int myDimension) {
		return calibration.getGravity(myDimension);
	}

	public int getCounter() {
		return (counter - 1) % 4 + 1;
	}

	private static int moteCounter = 0;

	private static Set<MoteFinderListener> listenerSet = new HashSet<MoteFinderListener>();

	public static void startMoteDiscovery(final MoteFoundListener listener) {
		MoteFinderListener moteFinderListener = new MoteFinderListener() {
			public void moteFound(Mote myMote) {
				Acc3DSensor.stopMoteDiscovery();
				listener.moteFound(new Acc3DSensor().connect(myMote));
			}
		};
		Acc3DSensor.listenerSet.add(moteFinderListener);
		try {
			MoteFinder.getMoteFinder().addMoteFinderListener(moteFinderListener);

			MoteFinder.getMoteFinder().startDiscovery();
		} catch (RuntimeException rex) {
			if (rex.getCause().getClass().equals(BluetoothStateException.class)) {
				System.err.println(Messages.getString("SensorConnectDialog.DeviceNotFound"));
			}
		}
	}

	public static void stopMoteDiscovery() {
		try {
			MoteFinder.getMoteFinder().stopDiscovery();
		} catch (RuntimeException rex) {
			if (rex.getCause().getClass().equals(BluetoothStateException.class)) {
				System.err.println(Messages.getString("SensorConnectDialog.DeviceNotFound"));
			}
		}

		for (MoteFinderListener listener : Acc3DSensor.listenerSet) {
			try {
				Acc3DSensor.listenerSet.remove(listener);
				MoteFinder.getMoteFinder().removeMoteFinderListener(listener);
			} catch (RuntimeException rex) {
				if (rex.getCause().getClass().equals(BluetoothStateException.class)) {
					System.err.println(Messages.getString("SensorConnectDialog.DeviceNotFound"));
				}
			}

		}
	}

}
