package org.phymote.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.data.Range;
import org.phymote.control.listener.MouseClickListener;

/**
 * extended ChartPanel, to be used in ChartInternalFrame and
 * VirtualSensorChartInternalFrame. enables zooming via scroll-wheel.
 * 
 * @author Christoph Krichenbauer
 * 
 */
public class SensorChartPanel extends ChartPanel {

	private static final long serialVersionUID = -2954285962124194979L;

	public SensorChartPanel(JFreeChart chart) {

		super(chart, 320, 340, 320, 340, 1200, 1200, true, true, true, false, true, true);
		setPreferredSize(new Dimension(320, 340));
		getChart().getXYPlot().getRangeAxis().setAutoRange(true);
		getChart().getXYPlot().getDomainAxis().setAutoRange(true);

		// zoom by scrolling:
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				getChart().getXYPlot().getRangeAxis().setAutoRange(false);
				getChart().getXYPlot().getDomainAxis().setAutoRange(false);
				if (e.getWheelRotation() > 0) {
					if (e.isShiftDown()) {
						getChart().getXYPlot().getRangeAxis().setRange(
								Range.shift(getChart().getXYPlot().getRangeAxis().getRange(), -getChart().getXYPlot().getRangeAxis().getRange().getLength() / 20));
					} else if (e.isControlDown()) {
						getChart().getXYPlot().getDomainAxis().setRange(
								Range.shift(getChart().getXYPlot().getDomainAxis().getRange(), getChart().getXYPlot().getDomainAxis().getRange().getLength() / 20));
					} else {
						zoomOutBoth(5.0, 5.0);
					}
				} else {
					if (e.isShiftDown()) {
						getChart().getXYPlot().getRangeAxis().setRange(
								Range.shift(getChart().getXYPlot().getRangeAxis().getRange(), getChart().getXYPlot().getRangeAxis().getRange().getLength() / 20));
					} else if (e.isControlDown()) {
						getChart().getXYPlot().getDomainAxis().setRange(
								Range.shift(getChart().getXYPlot().getDomainAxis().getRange(), -getChart().getXYPlot().getDomainAxis().getRange().getLength() / 20));
					} else {
						zoomInBoth(5.0, 5.0);
					}
				}
			}
		});

		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	private IntervalMarker domainMarker = null;
	private IntervalMarker rangeMarker = null;

	/**
	 * sets a new range to be marked (highlighted) on domain axis
	 * 
	 * @param marker
	 */
	public void setDomainMarker(IntervalMarker marker) {
		if (domainMarker != null) {
			getChart().getXYPlot().removeDomainMarker(domainMarker);
		}
		if (marker != null) {
			marker.setPaint(Color.LIGHT_GRAY);
			marker.setAlpha(0.2f);

			getChart().getXYPlot().addDomainMarker(marker);
			domainMarker = marker;
		}

	}

	/**
	 * sets a new range to be marked (highlighted) on range axis.
	 * 
	 * @param marker
	 */
	public void setRangeMarker(IntervalMarker marker) {
		if (rangeMarker != null) {
			getChart().getXYPlot().removeRangeMarker(rangeMarker);
		}
		if (marker != null) {
			marker.setPaint(Color.LIGHT_GRAY);
			marker.setAlpha(0.2f);

			getChart().getXYPlot().addRangeMarker(marker);
			rangeMarker = marker;
		}
	}

	public IntervalMarker getDomainMarker() {
		return domainMarker;
	}

	public IntervalMarker getRangeMarker() {
		return rangeMarker;
	}

	/**
	 * Toggels AxisTraxe (i.e. crosshair)
	 * 
	 * @param on
	 *            true, if you want to enable, false to disable
	 */
	private void setAxisTrace(boolean on) {
		setVerticalAxisTrace(on);
		setHorizontalAxisTrace(on);
	}

	/**
	 * Check if axistrace is enabled
	 * 
	 * @return true if yes.
	 */
	public boolean getAxisTrace() {
		return (getVerticalAxisTrace() || getHorizontalAxisTrace());
	}

	/**
	 * Add crosshair functionality. clicking disables.
	 */
	public void enableAxisTrace() {
		setAxisTrace(true);
		addMouseListener(new MouseClickListener() {
			public void mousePressed(MouseEvent e) {
				removeMouseListener(this);
				setAxisTrace(false);
			}
		});
	}
}
