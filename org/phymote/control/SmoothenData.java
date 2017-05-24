package org.phymote.control;

public class SmoothenData {

	/**
	 * Smoothen on the left side.
	 */
	public static final int LEFT = 0;

	/**
	 * Smoothen symetric
	 */
	public static final int CENTER = 1;

	/**
	 * Smoothen on the right side
	 */
	public static final int RIGHT = 2;

	/**
	 * Use Median as mean value
	 */
	public static final int MEDIAN = 0;

	/**
	 * Use arithmetic mean as mean value
	 */
	public static final int ARITHMETIC = 1;

	private int orientation;
	private int length;
	private int type;

	/**
	 * SmoothenData Main Constructor
	 * 
	 * @param myOrientation
	 *            Orientation (Left, Center, Right)
	 * @param myType
	 *            Mean-type (Median, Arithmetic)
	 * @param myLength
	 *            Length to smooth (in ms)
	 */
	public SmoothenData(int myOrientation, int myType, int myLength) {
		orientation = myOrientation;
		length = myLength;
		type = myType;
	}

	/**
	 * @return length (in ms) to smoothen
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return orientation (left, center, right)
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * @return mean type (median, arithmetic)
	 */
	public int getType() {
		return type;
	}

}
