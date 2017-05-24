package org.phymote.sensor.data;

import org.phymote.sensor.datarow.PhDataRow;

public class Acc3DData implements PhData {
	private final int dimensions = 3;
	private int mTime;
	private int[] values;
	private PhDataRow dataSet;

	public Acc3DData(PhDataRow myDataSet, int myXAcc, int myYAcc, int myZAcc) {
		dataSet = myDataSet;
		mTime = (dataSet.getTimeShift() != 0 ? (int) (System.nanoTime() / 1000000) - dataSet.getTimeShift() : 0);
		values = new int[] { myXAcc, myYAcc, myZAcc };
	}

	@Override
	public String toString() {
		return getSCString();
	}

	public String getSCString() {
		return mTime + ";" + dataSet.getSensor().getCalibratedValue(0, values[0]) + ";" + dataSet.getSensor().getCalibratedValue(1, values[1]) + ";"
				+ dataSet.getSensor().getCalibratedValue(2, values[2]);
	}

	public PhDataRow getDataRow() {
		return dataSet;
	}

	public int getMTime() {
		return mTime;
	}

	public int getValue(int dimension) {
		return values[dimension];
	}

	public double getValueDouble(int dimension) {
		return values[dimension];
	}

	public float getCalibratedValue(int dimension) {
		return dataSet.getSensor().getCalibratedValue(dimension, getValue(dimension));
	}

	public int getDimension() {
		return dimensions;
	}

	public void setDataRow(PhDataRow dataRow) {
		dataSet = dataRow;
	}

}
