package org.phymote.sensor.data;

import org.phymote.sensor.datarow.PhDataRow;

public interface PhData {
	public PhDataRow getDataRow();

	public int getMTime();

	public int getValue(int dimension);

	public double getValueDouble(int dimension);

	public float getCalibratedValue(int dimension);

	public int getDimension();

	public void setDataRow(PhDataRow myDataRow);

	/**
	 * Generate Excel-compatible Semi-colon seperated Data
	 * 
	 * @return String of [time;x;y;z;\n]
	 */
	public String getSCString();
}
