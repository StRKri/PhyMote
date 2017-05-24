package org.phymote.sensor.datarow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.phymote.control.SmoothenData;
import org.phymote.control.exceptions.MergeDimensionUnitNotMatchException;
import org.phymote.sensor.ChartModificationSensor;
import org.phymote.sensor.PhSensor;
import org.phymote.sensor.data.PhData;
import org.phymote.sensor.data.VirtualData;
import org.phymote.statics.StringHelper;

public class ChartModificationDataRow implements PhDataRow {
	private PhDataRow parentPhDataRow;
	private ArrayList<PhData> dataList;
	private JFreeChart cacheChart;
	protected XYSeries[] chartDataSeries;
	private XYSeriesCollection chartDataSet;
	private ChartModificationSensor sensor;
	private int dimensions = 0;
	private String[] dimensionDesc = null;
	private String[] units = null;

	public ChartModificationDataRow(ChartModificationSensor mySensor, PhDataRow myParent) {
		sensor = mySensor;
		parentPhDataRow = myParent;
		dataList = new ArrayList<PhData>();
		for (int i = 0; i < parentPhDataRow.getDataCount(); i++) {
			dataList.add(parentPhDataRow.getData(i));
			parentPhDataRow.getData(i).setDataRow(this);
		}
	}

	public ChartModificationDataRow(ChartModificationSensor mySensor) {
		parentPhDataRow = null;
		dataList = new ArrayList<PhData>();
		sensor = mySensor;
	}

	public void addData(PhData myData) {
		dataList.add(myData);
	}

	public JFreeChart getChart() {
		if ((cacheChart == null) || (cacheChart.getXYPlot().getSeriesCount() != getDimension())) {
			cacheChart = generateNewChart();
		}
		return cacheChart;
	}

	public JFreeChart getChart(int dimension) {
		cacheChart = null;
		boolean dimensions[] = new boolean[getDimension()];
		dimensions[dimension] = true;
		return generateNewChart(dimensions);
	}

	public JFreeChart getChart(boolean[] dimensions) {
		cacheChart = generateNewChart(dimensions);
		return cacheChart;
	}

	public int getVisibleDimensions() {
		return cacheChart.getXYPlot().getSeriesCount();
	}

	private JFreeChart generateNewChart() {
		boolean[] myDimensions = new boolean[getDimension()];
		for (int i = 0; i < getDimension(); i++) {
			myDimensions[i] = true;
		}
		return generateNewChart(myDimensions);
	}

	private JFreeChart generateNewChart(boolean[] dimensions) {
		chartDataSeries = new XYSeries[getDimension()];
		chartDataSet = new XYSeriesCollection();

		int dim = 0;
		for (boolean dimension : dimensions) {
			if (dimension) {
				dim++;
			}
		}
		String axisLegends[] = new String[dim];
		dim = 0;
		boolean different = false;

		for (int i = 0; i < dimensions.length; i++) {
			if (dimensions[i]) {
				chartDataSeries[i] = new XYSeries(getDesc(i));
				chartDataSet.addSeries(chartDataSeries[i]);
				axisLegends[dim] = getUnit(i);
				if ((dim > 0) && !axisLegends[dim - 1].equals(axisLegends[dim])) {
					different = true;
				}
				dim++;
			}
		}
		String yAxisLegend;
		if (different) {
			yAxisLegend = StringHelper.join("/", axisLegends);
		} else {
			yAxisLegend = axisLegends[0];
		}

		for (int j = 0; j < dataList.size(); j++) {
			for (int i = 0; i < dimensions.length; i++) {
				if (dimensions[i]) {
					chartDataSeries[i].add(dataList.get(j).getMTime(), dataList.get(j).getCalibratedValue(i));
				}
			}
		}

		JFreeChart chart = ChartFactory.createXYLineChart("", "time", yAxisLegend, chartDataSet, PlotOrientation.VERTICAL, true, true, true);
		chart.setAntiAlias(false);
		chart.getXYPlot().setBackgroundPaint(Color.WHITE);
		chart.getXYPlot().setDomainGridlinePaint(Color.GRAY);
		chart.getXYPlot().setRangeGridlinePaint(Color.GRAY);

		return chart;
	}

	public int getDimension() {
		if (dimensions == 0) {
			return getSensor().getDimension();
		} else {
			return dimensions;
		}

	}

	public int getLastMTime() {
		if (parentPhDataRow != null) {
			return parentPhDataRow.getLastMTime();
		} else {
			return dataList.get(dataList.size()).getMTime();
		}
	}

	ArrayList<PhData> getDataList() {
		return dataList;
	}

	public PhSensor getSensor() {
		return sensor;
	}

	public int getTimeShift() {
		return parentPhDataRow.getTimeShift();
	}

	@Override
	public PhDataRow clone() {
		return null;
	}

	public PhData getData(int i) {
		return dataList.get(i);
	}

	public int getDataCount() {
		return dataList.size();
	}

	public void invert() {
		boolean[] visibleDimensions = new boolean[getDimension()];
		for (int i = 0; i < visibleDimensions.length; i++) {
			visibleDimensions[i] = true;
		}
		invert(visibleDimensions);
	}

	public void invert(boolean[] visibleDimensions) {
		for (int i = 0; i < visibleDimensions.length; i++) {
			((ChartModificationSensor) getSensor()).setCalibrateRepresentValue(i, 0.0f - getSensor().getCalibrateRepresentValue(i));
		}
		cacheChart = null;

	}

	public void smoothen(SmoothenData smoothenData) {
		boolean[] visibleDimensions = new boolean[getDimension()];
		for (int i = 0; i < visibleDimensions.length; i++) {
			visibleDimensions[i] = true;
		}
		smoothen(smoothenData, visibleDimensions);
	}

	public void smoothen(SmoothenData smoothenData, boolean[] visibleDimensions) {
		ArrayList<PhData> myDataList = new ArrayList<PhData>();

		for (int i = 0; i < dataList.size() - 1; i++) {
			if (dataList.get(i).getMTime() < smoothenData.getLength()) {
				// do nothing!
			} else if (dataList.get(i).getMTime() + smoothenData.getLength() > dataList.get(dataList.size() - 1).getMTime()) {
				// do nothing
			} else {
				double[] smoothValue = new double[getDimension()];
				for (int j = 0; j < getDimension(); j++) {
					if (visibleDimensions[j]) {
						ArrayList<Double> medianList = new ArrayList<Double>();
						double sum = 0;
						int counter = 0;
						for (int k = -smoothenData.getLength() / 5; k <= smoothenData.getLength() / 5; k++) {
							try {
								if (((Math.abs(dataList.get(i + k).getMTime() - dataList.get(i).getMTime()) <= smoothenData.getLength()) && (smoothenData.getOrientation() == SmoothenData.CENTER))
										|| ((Math.abs(dataList.get(i + k).getMTime() - dataList.get(i).getMTime()) <= smoothenData.getLength() * 2)
												&& (dataList.get(i + k).getMTime() > dataList.get(i).getMTime()) && (smoothenData.getOrientation() == SmoothenData.LEFT))
										|| ((Math.abs(dataList.get(i + k).getMTime() - dataList.get(i).getMTime()) <= smoothenData.getLength() * 2)
												&& (dataList.get(i + k).getMTime() < dataList.get(i).getMTime()) && (smoothenData.getOrientation() == SmoothenData.RIGHT))) {
									if (smoothenData.getType() == SmoothenData.MEDIAN) {
										medianList.add(dataList.get(i + k).getValueDouble(j));
									} else {
										sum += dataList.get(i + k).getValueDouble(j);
									}
									counter++;
								}
							} catch (IndexOutOfBoundsException e) {
								// no problem: do nothing.
							}
						}
						if (smoothenData.getType() == SmoothenData.MEDIAN) {
							Collections.sort(medianList);
							smoothValue[j] = medianList.get((medianList.size() - 1) / 2).doubleValue();
						} else {
							smoothValue[j] = sum / counter;
						}
					} else {
						smoothValue[j] = dataList.get(i).getValueDouble(j);
					}
				}
				myDataList.add(new VirtualData(this, dataList.get(i).getMTime(), smoothValue));
			}
		}
		cacheChart = null;
		dataList = myDataList;
	}

	public void mergeDimensions() throws MergeDimensionUnitNotMatchException {
		boolean[] visibleDimensions = new boolean[getDimension()];
		for (int i = 0; i < visibleDimensions.length; i++) {
			visibleDimensions[i] = true;
		}
		mergeDimensions(visibleDimensions);
	}

	public void mergeDimensions(boolean[] visibleDimensions) throws MergeDimensionUnitNotMatchException {
		ArrayList<PhData> myDataList = new ArrayList<PhData>();

		// new dimension: every visible --> only one (+ every invisible itself)
		int newDimensionCount = 1;
		int dimensionMapper[] = new int[getDimension()];
		String newUnit = null;
		for (int dim = 0; dim < visibleDimensions.length; dim++) {
			if (visibleDimensions[dim]) {
				dimensionMapper[dim] = 0;
				if (newUnit == null) {
					newUnit = getUnit(dim);
				} else {
					if (!getUnit(dim).equals(newUnit)) {
						throw new MergeDimensionUnitNotMatchException();
					}
				}
			} else {
				dimensionMapper[dim] = newDimensionCount;
				newDimensionCount++;
			}
		}

		// set descriptions:
		String[] newDimensionDesc = new String[newDimensionCount];
		for (int dim = 0; dim < visibleDimensions.length; dim++) {
			if (newDimensionDesc[dimensionMapper[dim]] == null) {
				newDimensionDesc[dimensionMapper[dim]] = getDesc(dim);
			} else {
				newDimensionDesc[dimensionMapper[dim]] += "/" + getDesc(dim);
			}
		}

		dimensionDesc = newDimensionDesc;

		int[] myZeros = new int[getDimension()];
		int[] myCalibrates = new int[getDimension()];
		float[] myRepresents = new float[getDimension()];
		String[] myUnits = new String[getDimension()];
		for (int i = 0; i < newDimensionCount; i++) {
			myZeros[i] = 0;
			myCalibrates[i] = 1;
			myRepresents[i] = 1;
			myUnits[i] = getUnit(dimensionMapper[i]);
		}

		for (int i = 0; i < dataList.size(); i++) {
			double[] newValue = new double[newDimensionCount];
			for (int j = 0; j < newValue.length; j++) {
				newValue[j] = 0.0;
			}
			for (int j = 0; j < getDimension(); j++) {
				newValue[dimensionMapper[j]] = Math.sqrt(newValue[dimensionMapper[j]] * newValue[dimensionMapper[j]] + dataList.get(i).getCalibratedValue(j)
						* dataList.get(i).getCalibratedValue(j));
			}
			myDataList.add(new VirtualData(this, dataList.get(i).getMTime(), newValue));
		}
		getSensor().setCalibration(myZeros, myCalibrates, myRepresents, myUnits);

		setDimension(newDimensionCount);
		units = myUnits;

		cacheChart = null;
		dataList = myDataList;
	}

	//überflüssig??!
	// public void smoothenMedian(int smoothInterval, boolean[]
	// visibleDimensions) {
	// ArrayList<PhData> myDataList = new ArrayList<PhData>();
	// for (int i = smoothInterval; i < dataList.size() - 2 * smoothInterval;
	// i++) {
	// int[] smoothValue = new int[getDimension()];
	// for (int j = 0; j < getDimension(); j++) {
	// if (visibleDimensions[j]) {
	// int[] medianList = new int[2 * smoothInterval + 1];
	// int counter=0;
	// for (int k = -smoothInterval; k <= smoothInterval; k++) {
	// medianList[counter]=dataList.get(i + k).getValue(j);
	// counter++;
	// }
	// Arrays.sort(medianList);
	// smoothValue[j] = medianList[smoothInterval+1];
	// } else {
	// smoothValue[j] = dataList.get(i).getValue(j);
	// }
	// }
	// myDataList.add(new VirtualData(this, (dataList.get(
	// i + smoothInterval).getMTime() + dataList.get(
	// i - smoothInterval).getMTime()) / 2, smoothValue));
	// }
	// cacheChart = null;
	// dataList = myDataList;
	// }

	private void setDimension(int newDimensionCount) {
		dimensions = newDimensionCount;

	}

	public void cutTime(IntervalMarker intervalMarker) {
		ArrayList<PhData> myDataList = new ArrayList<PhData>();
		double lowerBound = intervalMarker.getStartValue();
		double upperBound = intervalMarker.getEndValue();

		for (int i = 0; i < dataList.size(); i++) {
			if ((dataList.get(i).getMTime() > lowerBound) && (dataList.get(i).getMTime() < upperBound)) {
				myDataList.add(dataList.get(i));
			}
		}

		cacheChart = null;
		dataList = myDataList;
	}

	public void integrate(int myDimension) {
		ArrayList<PhData> myDataList = new ArrayList<PhData>();

		double[] myFirstValues = new double[getDimension()];

		for (int i = 0; i < getDimension(); i++) {
			if (i == myDimension) {
				// myFirstValues[i] = getSensor().getCalibrationZero(i);
				myFirstValues[i] = 0.0;
			} else {
				myFirstValues[i] = dataList.get(0).getValueDouble(i);
			}
		}

		myDataList.add(new VirtualData(dataList.get(0).getDataRow(), dataList.get(0).getMTime(), myFirstValues));

		for (int i = 1; i < dataList.size(); i++) {

			double myValues[] = new double[getDimension()];
			for (int j = 0; j < getDimension(); j++) {
				if (j == myDimension) {
					// double my1st = dataList.get(i -
					// 1).getValueDouble(myDimension) -
					// getSensor().getCalibrationZero(myDimension);
					// double my2nd =
					// dataList.get(i).getValueDouble(myDimension) -
					// getSensor().getCalibrationZero(myDimension);
					double my1st = getSensor().getCalibratedValue(myDimension, dataList.get(i - 1).getValueDouble(myDimension));
					double my2nd = getSensor().getCalibratedValue(myDimension, dataList.get(i).getValueDouble(myDimension));
					int my1stTime = dataList.get(i - 1).getMTime();
					int my2ndTime = dataList.get(i).getMTime();
					myValues[j] = ((my2nd - my1st) / 2 + my1st) * ((my2ndTime - my1stTime) / 1000.0) + myDataList.get(i - 1).getValueDouble(myDimension);
				} else {
					myValues[j] = dataList.get(i).getValueDouble(j);
				}
			}

			myDataList.add(new VirtualData(this, dataList.get(i).getMTime(), myValues));
		}

		int[] myZeros = new int[getDimension()];
		int[] myCalibrates = new int[getDimension()];
		float[] myRepresents = new float[getDimension()];
		String[] myUnits = new String[getDimension()];

		for (int i = 0; i < getDimension(); i++) {
			if (i == myDimension) {
				// myZeros[i] = getSensor().getCalibrationZero(myDimension);
				// myCalibrates[i] = 10; //TODO: Wieso??
				// myRepresents[i] = 1; //TODO: Wieso??
				myZeros[i] = 0;
				myCalibrates[i] = 1; // TODO: Wieso??
				myRepresents[i] = 1; // TODO: Wieso??
				myUnits[i] = StringHelper.integrateUnit(getUnit(i));
			} else {
				myZeros[i] = getSensor().getCalibrationZero(i);
				myCalibrates[i] = getSensor().getCalibrationValue(i);
				myRepresents[i] = getSensor().getCalibrateRepresentValue(i);
				myUnits[i] = getUnit(i);
			}
		}

		getSensor().setCalibration(myZeros, myCalibrates, myRepresents, myUnits);

		setUnitsIfUsed(myUnits);
		getSensor().setDesc(myDimension, StringHelper.integrateSymbol(getDesc(myDimension)));

		cacheChart = null;
		dataList = myDataList;

	}

	public void updateChart() {
		// nothing to do.
	}

	public void calibrateZeros(boolean[] visibleDimensions, IntervalMarker domainMarker) {
		for (int dim = 0; dim < getDimension(); dim++) {
			if (visibleDimensions[dim]) {
				double mean_zero = 0.0;
				int counter = 0;
				for (int i = 0; i < dataList.size(); i++) {
					if ((dataList.get(i).getMTime() > domainMarker.getStartValue()) && (dataList.get(i).getMTime() < domainMarker.getEndValue())) {
						mean_zero = mean_zero + dataList.get(i).getValueDouble(dim);
						counter++;
					}
				}
				mean_zero = mean_zero / counter;
				sensor.setCalibrateZerosDouble(dim, mean_zero);
			}
		}
		cacheChart = null;
	}

	public String getSCString() {
		StringBuffer resultBuffer = new StringBuffer("time;");
		for (int dim = 0; dim < getDimension(); dim++) {
			resultBuffer.append(getDesc(dim) + ";");
		}
		resultBuffer.append("\n");

		for (int i = 0; i < dataList.size(); i++) {
			resultBuffer.append(dataList.get(i).getSCString() + "\n");
		}
		return resultBuffer.toString();
	}

	public void setSensor(ChartModificationSensor chartModificationSensor) {
		sensor = chartModificationSensor;
	}

	public String getDesc(int i) {
		if (dimensionDesc == null) {
			return getSensor().getDesc(i);
		} else {
			return dimensionDesc[i];
		}
	}

	public String getUnit(int i) {
		if (units == null) {
			return getSensor().getUnit(i);
		} else {
			return units[i];
		}
	}

	private void setUnitsIfUsed(String[] myUnits) {
		if (units != null) {
			units = myUnits;
		}
	}

}
