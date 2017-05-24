package org.phymote.sensor.data;

import org.phymote.sensor.datarow.PhDataRow;

public class VirtualData implements PhData {
	private int dimensions;
	protected int mTime;
	protected double[] values;
	protected PhDataRow dataSet;

	public VirtualData(PhDataRow myDataSet, int myMTime, int[] myValues) {
		dataSet = myDataSet;
		dimensions = myValues.length;
		values = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			values[i] = myValues[i];
		}
		mTime = myMTime;
	}

	public VirtualData(PhDataRow myDataSet, int myMTime, double[] myValues) {
		dataSet = myDataSet;
		dimensions = myValues.length;
		values = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			values[i] = myValues[i];
		}
		mTime = myMTime;
	}

	@Override
	public String toString() {
		return getSCString();
	}

	public String getSCString() {
		String result = mTime + ";";
		for (int i = 0; i < dimensions; i++) {
			result += dataSet.getSensor().getCalibratedValue(i, values[i]) + ";";
		}
		return result;
	}

	public PhDataRow getDataRow() {
		return dataSet;
	}

	public int getMTime() {
		return mTime;
	}

	public int getValue(int dimension) {
		return (int) values[dimension];
	}

	public double getValueDouble(int dimension) {
		return values[dimension];
	}

	public float getCalibratedValue(int dimension) {
		return dataSet.getSensor().getCalibratedValue(dimension, getValueDouble(dimension));
	}

	public int getDimension() {
		return dimensions;
	}

	public void setDataRow(PhDataRow myDataRow) {
		dataSet = myDataRow;
	}

}
