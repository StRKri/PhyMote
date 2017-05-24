package org.phymote.control.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.HashSet;

import org.jfree.ui.RectangleEdge;
import org.phymote.gui.SensorChartPanel;

/**
 * Listener for Charts. Memorizes double-clicks to inform given RangeClickedListeners about double-clicked distance.
 * @author Christoph Krichenbauer 
 */
public class ChartRangeMouseListener extends MouseClickListener {
	private SensorChartPanel chartPanel;
	private Point2D firstClick = null;
	private Point2D secondClick = null;
	private HashSet<RangeClickedListener> listeners;
	private MouseMotionListener motionListener;

	public ChartRangeMouseListener(SensorChartPanel sensorChartPanel) {
		chartPanel = sensorChartPanel;
		listeners = new HashSet<RangeClickedListener>();

		motionListener = new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				// nothing;
			}

			public void mouseMoved(MouseEvent e) {
				informRangeClickedListeners(new Point2D[] {
						firstClick,
						new Point2D.Double(chartPanel.getChart().getXYPlot().getDomainAxis().java2DToValue(e.getPoint().getX(), chartPanel.getScreenDataArea(),
								RectangleEdge.BOTTOM), chartPanel.getChart().getXYPlot().getRangeAxis().java2DToValue(e.getPoint().getY(), chartPanel.getScreenDataArea(),
								RectangleEdge.LEFT)) });

			}

		};

	}

	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if (firstClick == null) {

				informRemoveRangeClickedListeners();
				firstClick = new Point2D.Double(chartPanel.getChart().getXYPlot().getDomainAxis().java2DToValue(e.getPoint().getX(), chartPanel.getScreenDataArea(),
						RectangleEdge.BOTTOM), chartPanel.getChart().getXYPlot().getRangeAxis().java2DToValue(e.getPoint().getY(), chartPanel.getScreenDataArea(),
						RectangleEdge.LEFT));

				chartPanel.addMouseMotionListener(motionListener);

			} else if (secondClick == null) {
				chartPanel.removeMouseMotionListener(motionListener);
				secondClick = new Point2D.Double(chartPanel.getChart().getXYPlot().getDomainAxis().java2DToValue(e.getPoint().getX(), chartPanel.getScreenDataArea(),
						RectangleEdge.BOTTOM), chartPanel.getChart().getXYPlot().getRangeAxis().java2DToValue(e.getPoint().getY(), chartPanel.getScreenDataArea(),
						RectangleEdge.LEFT));

				informRangeClickedListeners(new Point2D[] { firstClick, secondClick });

			} else {
				firstClick = null;
				secondClick = null;
				informRangeClickedListeners(null);
			}

		}
	}

	/**
	 * Add listener (to be informed on double-clicked ranges)
	 * 
	 * @param l
	 *            listener
	 */
	public void addRangeClickedListener(RangeClickedListener l) {
		listeners.add(l);
	}

	/**
	 * Remove listener
	 * 
	 * @param l
	 *            listener
	 */
	public void removeRangeClickedListener(RangeClickedListener l) {
		listeners.remove(l);
	}

	private void informRangeClickedListeners(Point2D[] point2Ds) {
		try {
			RangeClickedEvent e = new RangeClickedEvent(point2Ds[0], point2Ds[1]);
			for (RangeClickedListener l : listeners) {
				l.rangeClicked(e);
			}
		} catch (NullPointerException ex) {
			for (RangeClickedListener l : listeners) {
				l.removeRange();
			}
		}
	}

	private void informRemoveRangeClickedListeners() {
		for (RangeClickedListener l : listeners) {
			l.removeRange();
		}
	}

}
