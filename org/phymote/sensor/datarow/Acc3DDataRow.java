package org.phymote.sensor.datarow;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.phymote.sensor.PhSensor;
import org.phymote.sensor.data.Acc3DData;
import org.phymote.sensor.data.PhData;

public class Acc3DDataRow implements PhDataRow {
	private final int dimensions = 3;
	private JFreeChart chart;
	protected XYSeries[] chartDataSeries;
	private XYSeriesCollection chartDataSet;
	protected ArrayList<PhData> dataList;
	private int timeShift;
	private PhSensor sensor;
	private int addCounter = 0;

	public Acc3DDataRow(PhSensor myPhSensor) {
		dataList = new ArrayList<PhData>();
		sensor = myPhSensor;
		timeShift = 0;

		chartDataSeries = new XYSeries[dimensions];
		chartDataSet = new XYSeriesCollection();

		for (int i = 0; i < dimensions; i++) {
			chartDataSeries[i] = new XYSeries(sensor.getDesc(i));
			chartDataSet.addSeries(chartDataSeries[i]);
		}

		chart = ChartFactory.createXYLineChart("", "ms", "m/s²", chartDataSet, PlotOrientation.VERTICAL, true, true, true);
		chart.setAntiAlias(false);
	}

	private void setTimeShift(int myTimeShift) {
		timeShift = myTimeShift;
	}

	public int initTimeShift(int myTimeShift) {
		if (timeShift == 0) {
			setTimeShift(myTimeShift);
		}
		return timeShift;
	}

	public int initTimeShift() {
		return this.initTimeShift((int) (System.nanoTime() / 1000000));
	}

	public int getTimeShift() {
		return timeShift;
	}

	public int getDimension() {
		return dimensions;
	}

	public PhSensor getSensor() {
		return sensor;
	}

	public void addData(PhData myData) {
		this.initTimeShift();
		if ((dataList.size() == 0) || (dataList.get(dataList.size() - 1).getMTime() < myData.getMTime())) {
			dataList.add(myData);
			if (addCounter > 9) {
				for (int i = 0; i < dimensions; i++) {
					chartDataSeries[i].add(myData.getMTime(), myData.getCalibratedValue(i), true);
				}
				addCounter = 0;
			} else {
				for (int i = 0; i < dimensions; i++) {
					chartDataSeries[i].add(myData.getMTime(), myData.getCalibratedValue(i), false);
				}
				addCounter++;
			}
		}
	}

	public void addAcc3DValue(int myXAcc, int myYAcc, int myZAcc) {
		this.initTimeShift();
		addData(new Acc3DData(this, myXAcc, myYAcc, myZAcc));
	}

	public JFreeChart getChart() {
		return chart;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < dataList.size(); i++) {
			result += dataList.get(i).toString() + "\n";
		}
		return result;
	}

	public String getSCString() {
		StringBuffer resultBuffer = new StringBuffer("time;a_x;a_y;a_z;\n");

		for (int i = 0; i < dataList.size(); i++) {
			resultBuffer.append(dataList.get(i).getSCString() + "\n");
		}
		return resultBuffer.toString();
	}

	public int getMeanValue(int dimension) {
		long sum = 0;
		for (int i = 0; i < dataList.size(); i++) {
			sum = sum + dataList.get(i).getValue(dimension);
		}
		return (int) (sum / (float) dataList.size());
	}

	public int getLastMTime() {
		if (dataList.size() == 0) {
			return 0;
		} else {
			return (dataList.get(dataList.size() - 1).getMTime());
		}
	}

	public PhData getData(int i) {
		return dataList.get(i);
	}

	public int getDataCount() {
		return dataList.size();
	}

	// required due to performance
	public void updateChart() {
		chartDataSet.removeAllSeries();
		for (int i = 0; i < dimensions; i++) {
			chartDataSeries[i] = new XYSeries(getSensor().getDesc(i));
			for (int j = 0; j < dataList.size(); j++) {
				chartDataSeries[i].add(dataList.get(j).getMTime(), dataList.get(j).getCalibratedValue(i));
			}
			chartDataSet.addSeries(chartDataSeries[i]);
		}
		chart.fireChartChanged();
	}

}
