package org.phymote.sensor;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.ValueDataset;
import org.phymote.persistance.SensorXML;
import org.phymote.sensor.datarow.ChartModificationDataRow;
import org.phymote.sensor.datarow.PhDataRow;

public class ChartModificationSensor extends NameablePhSensor implements PhSensor {
	private int dimensions;
	private double[] calibrateZerosDouble;
	private int[] calibrateValues;
	private float[] calibrateRepresentValues;
	private String[] units;
	private int counter;

	private ChartModificationDataRow activeDataRow;
	private PhSensor parentSensor;
	private boolean sensorDataChanged;

	public ChartModificationSensor(PhSensor mySensor, boolean numberRequired) {
		this(mySensor);
		if (!numberRequired) {
			ChartModificationSensor.releaseCounter(counter);
			counter = -1;
		}
	}

	public ChartModificationSensor(File saveFile, boolean numberRequired) {
		this(saveFile);
		if (!numberRequired) {
			ChartModificationSensor.releaseCounter(counter);
			counter = -1;
		}
	}

	public ChartModificationSensor(PhSensor mySensor) {
		parentSensor = mySensor;
		activeDataRow = new ChartModificationDataRow(this, mySensor.getActiveDataRow());
		setName(parentSensor.getName() + " virtual");
		setDesc(parentSensor.getDescArray());
		sensorDataChanged = false;
		counter = ChartModificationSensor.grepCounter();
	}

	/**
	 * Generate new ChartModificationSensor by given XML-Savefile
	 * 
	 * @param savefile
	 *            Saved XML-File with Sensor Data
	 */
	public ChartModificationSensor(File savefile) {
		try {
			SensorXML sensorXML;
			sensorXML = new SensorXML(new ZipFile(savefile));
			sensorXML.makeSensor(this);
			counter = ChartModificationSensor.grepCounter();
		} catch (ZipException e) {
			System.err.println("Error reading zipped settings file.");
		} catch (IOException e) {
			System.err.println("Error reading zipped settings file.");
		}
	}

	public void captureData() {
		// do nothing
	}

	public PhSensor connect() {
		return this;
	}

	public void disconnect() {
		// do nothing
	}

	public ChartModificationDataRow getActiveDataRow() {
		return activeDataRow;
	}

	public float getCalibratedValue(int myDimension, int value) {
		if (!sensorDataChanged) {
			return parentSensor.getCalibratedValue(myDimension, value);
		} else {
			return calibrateRepresentValues[myDimension]
			// * (((float) value -
					// (int)Math.round(calibrateZerosDouble[myDimension])) /
					// calibrateValues[myDimension]);
					* ((value - (float) (calibrateZerosDouble[myDimension])) / calibrateValues[myDimension]);
		}
	}

	public float getCalibratedValue(int myDimension, double value) {
		if (!sensorDataChanged) {
			return parentSensor.getCalibratedValue(myDimension, value);
		} else {
			return calibrateRepresentValues[myDimension]
			// * (((float) value -
					// (int)Math.round(calibrateZerosDouble[myDimension])) /
					// calibrateValues[myDimension]);
					* (((float) value - (float) (calibrateZerosDouble[myDimension])) / calibrateValues[myDimension]);
		}

	}

	public ValueDataset getCalibratedValue(int dimension) {
		return null;
	}

	public JFreeChart getCalibratedValueChart(int myDimension) {
		return null;
	}

	public int getDimension() {
		if (!sensorDataChanged) {
			return parentSensor.getDimension();
		} else {
			return dimensions;
		}

	}

	public String getUnit(int dimension) {
		if (!sensorDataChanged) {
			return parentSensor.getUnit(dimension);
		} else {
			return units[dimension]; // TODO
		}
	}

	public void setCalibration(int[] zeros, int[] valueInts, float[] valueFloats, String[] myUnits) {
		sensorDataChanged = true;
		calibrateZerosDouble = new double[zeros.length];
		for (int i = 0; i < zeros.length; i++) {
			calibrateZerosDouble[i] = zeros[i];
		}
		calibrateValues = valueInts;
		calibrateRepresentValues = valueFloats;
		units = myUnits;

		dimensions = zeros.length; // das ist aber nicht schön.

	}

	private void cloneParentCalibration() {
		if (!sensorDataChanged) {
			sensorDataChanged = true;
			dimensions = parentSensor.getDimension();
			calibrateZerosDouble = new double[dimensions];
			calibrateValues = new int[dimensions];
			calibrateRepresentValues = new float[dimensions];
			units = new String[dimensions];
			for (int i = 0; i < dimensions; i++) {
				calibrateZerosDouble[i] = parentSensor.getCalibrationZero(i);
				calibrateValues[i] = parentSensor.getCalibrationValue(i);
				calibrateRepresentValues[i] = parentSensor.getCalibrateRepresentValue(i);
				units[i] = parentSensor.getUnit(i);
			}
		}
	}

	public void setActiveDataRow(PhDataRow myData) {
		// do nothing! Sensor always contains only one dataRow
	}

	public void setDataRow(ChartModificationDataRow dataRow) {
		activeDataRow = dataRow;
	}

	public void setNewDefaultDataRow() {
		// do nothing!
	}

	public void stopCaptureData() {
		// do nothing!
	}

	public static ChartModificationSensor getChartModificationSensor(PhSensor mySensor) {
		if (mySensor.getClass() == ChartModificationSensor.class) {
			return (ChartModificationSensor) mySensor;
		} else {
			return null;
		}
	}

	public boolean isVirtualSensor() {
		return true;
	}

	public int getCalibrationZero(int myDimension) {
		if (!sensorDataChanged) {
			return parentSensor.getCalibrationZero(myDimension);
		} else {
			return (int) Math.round(calibrateZerosDouble[myDimension]);
		}
	}

	public Float getCalibrateRepresentValue(int myDimension) {
		if (!sensorDataChanged || (calibrateRepresentValues == null)) {
			return parentSensor.getCalibrateRepresentValue(myDimension);
		} else {
			return calibrateRepresentValues[myDimension];
		}
	}

	public int getCalibrationValue(int myDimension) {
		if (!sensorDataChanged || (calibrateValues == null)) {
			return parentSensor.getCalibrationValue(myDimension);
		} else {
			return calibrateValues[myDimension];
		}
	}

	public void saveXML(File savefile) {
		SensorXML sensorXML = new SensorXML();
		sensorXML.setSensor(this, savefile.getName());
		sensorXML.setData(getActiveDataRow());
		sensorXML.saveZippedXML(savefile);
	}

	public void setCalibrateZerosDouble(int dim, double mean_zero) {
		try {
			calibrateZerosDouble[dim] = mean_zero;
		} catch (NullPointerException e) {
			cloneParentCalibration();
			calibrateZerosDouble[dim] = mean_zero;
		}
	}

	private ChartModificationSensor undoSensor;

	public ChartModificationDataRow getUndoDataRow() {
		return undoSensor.getActiveDataRow();
	}

	public void setUndoPoint() {
		undoSensor = new ChartModificationSensor(this, false);
		undoSensor.setCounter(getCounter());
		undoSensor.cloneParentCalibration();
		undoSensor.getActiveDataRow().setSensor(this);
	}

	public void undo() {
		try {
			// dimensions
			dimensions = undoSensor.getDimension();

			// calibration
			calibrateZerosDouble = new double[dimensions];
			calibrateValues = new int[dimensions];
			calibrateRepresentValues = new float[dimensions];
			units = new String[dimensions];

			for (int i = 0; i < dimensions; i++) {
				calibrateZerosDouble[i] = undoSensor.getCalibrationZero(i);
				calibrateValues[i] = undoSensor.getCalibrationValue(i);
				calibrateRepresentValues[i] = undoSensor.getCalibrateRepresentValue(i);
				setDesc(i, undoSensor.getDesc(i));
				units[i] = undoSensor.getUnit(i);
			}
			sensorDataChanged = true;

			// datarow
			activeDataRow = undoSensor.getActiveDataRow();

		} catch (NullPointerException e) {
			// undo impossible - do nothing
		}
	}

	private static boolean moteCounter[] = { false, false, false, false, false, false, false, false, false, false };

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public static int grepCounter() {
		for (int i = 0; i < ChartModificationSensor.moteCounter.length - 1; i++) {
			if (ChartModificationSensor.moteCounter[i] == false) {
				ChartModificationSensor.moteCounter[i] = true;
				return i + 1;
			}
		}
		return 0; // i.e. all (1-10) are overloaded.
	}

	public static void releaseCounter(int myCount) {
		ChartModificationSensor.moteCounter[myCount - 1] = false;
	}

	public void setCalibrateRepresentValue(int i, float f) {
		try {
			calibrateRepresentValues[i] = f;
		} catch (NullPointerException e) {
			cloneParentCalibration();
			calibrateRepresentValues[i] = f;
		}
	}

}
