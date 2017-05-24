package org.phymote.sensor.datarow;

import org.jfree.chart.JFreeChart;
import org.phymote.sensor.PhSensor;
import org.phymote.sensor.data.PhData;

/**
 * Interface Representing an DataRow
 */
public interface PhDataRow {
	public PhSensor getSensor();

	public int getDimension();

	public int getTimeShift();

	public JFreeChart getChart();

	public void addData(PhData myData);

	public int getLastMTime();

	public int getDataCount();

	public PhData getData(int i);

	public void updateChart();

	/**
	 * Generate Excel-compatible Semi-colon seperated Data
	 * 
	 * @return String of [time;x;y;z;\n]
	 */
	public String getSCString();

}
