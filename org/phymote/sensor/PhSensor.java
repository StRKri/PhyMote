package org.phymote.sensor;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.ValueDataset;
import org.phymote.sensor.datarow.PhDataRow;

/**
 * Interface representing any multi-dimensional Sensor to be used in PhMote All
 * Data is stored in int
 */
public interface PhSensor {
	/**
	 * get dimensions of this Sensor
	 * 
	 * @return dimension
	 */
	public int getDimension();

	/**
	 * Calibrates the Sensor for any available Dimensions
	 * 
	 * @param zeros
	 *            Array of Zero-shifts (i.e. X Y Z)
	 * @param valueInts
	 *            Array of integers for calculation of Real Values
	 * @param valueFloats
	 *            Real Values for valueInt
	 * @param units
	 *            Units for all Dimensions (normaly same)
	 */
	public void setCalibration(int[] zeros, int[] valueInts, float[] valueFloats, String[] units);

	/**
	 * Recalculates the input value using CalibrationData
	 * 
	 * @param value
	 *            sensor input
	 * @return calculated float output
	 */
	public float getCalibratedValue(int myDimension, int value);

	public float getCalibratedValue(int myDimension, double value);

	/**
	 * Get unit StringHelper set by setCalibration
	 * 
	 * @param dimension
	 * @return unit
	 */
	public String getUnit(int dimension);

	public void setActiveDataRow(PhDataRow myData);

	public void setNewDefaultDataRow();

	public PhDataRow getActiveDataRow();

	public PhSensor connect();

	public void disconnect();

	public void captureData();

	public void stopCaptureData();

	public ValueDataset getCalibratedValue(int dimension);

	public JFreeChart getCalibratedValueChart(int myDimension);

	public boolean isVirtualSensor();

	public int getCalibrationZero(int myDimension);

	public int getCalibrationValue(int myDimension);

	public int getCounter();

	public Float getCalibrateRepresentValue(int myDimension);

	public void setName(String name);

	public String getName();

	public void setDesc(int dimension, String desc);

	public void setDesc(String[] descArray);

	public String getDesc(int dimension);

	public String[] getDescArray();

}
