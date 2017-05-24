package org.phymote.control.listener;

import java.awt.geom.Point2D;

/**
 * @author Christoph Krichenbauer Event for ChartRangeMouseListener. Holds two
 *         Point2D, to carry about two double-click-locations.
 * 
 */
public class RangeClickedEvent {
	private Point2D firstPoint;
	private Point2D secondPoint;

	public RangeClickedEvent(Point2D f, Point2D s) {
		firstPoint = f;
		secondPoint = s;
	}

	/**
	 * get the left point's x value
	 * 
	 * @return left point's x value
	 */
	public double getLowX() {
		if (firstPoint.getX() < secondPoint.getX()) {
			return firstPoint.getX();
		} else {
			return secondPoint.getX();
		}
	}

	/**
	 * get the right point's x value
	 * 
	 * @return right point's x value
	 */
	public double getHighX() {
		if (firstPoint.getX() > secondPoint.getX()) {
			return firstPoint.getX();
		} else {
			return secondPoint.getX();
		}
	}

	/**
	 * get the bottom point's y value
	 * 
	 * @return bottom point's y value
	 */
	public double getLowY() {
		if (firstPoint.getY() < secondPoint.getY()) {
			return firstPoint.getY();
		} else {
			return secondPoint.getY();
		}
	}

	/**
	 * get the top point's y value
	 * 
	 * @return top point's y value
	 */
	public double getHighY() {
		if (firstPoint.getY() > secondPoint.getY()) {
			return firstPoint.getY();
		} else {
			return secondPoint.getY();
		}
	}

}
