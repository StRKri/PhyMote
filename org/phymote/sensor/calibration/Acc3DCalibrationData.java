package org.phymote.sensor.calibration;

import java.util.List;

import org.jdom.Element;
import org.phymote.persistance.Settings;

public class Acc3DCalibrationData {
	private final int dimensions = 3;

	private int[] calibrateZeros;
	private int[] calibrateValues;
	private float[] calibrateRepresentValues;
	private String[] units;
	private Settings settings;
	private Element configElement;
	private String bluetoothAddress;

	/**
	 * Basic Constructor. Initializes with more-or-less bad calibration.
	 * 
	 */
	public Acc3DCalibrationData() {
		settings = new Settings();
		// on initialize: calibrate with basic settings.
		calibrateZeros = new int[] { 127, 127, 127 };
		calibrateValues = new int[] { 20, 20, 20 };
		calibrateRepresentValues = new float[] { 9.81f, 9.81f, 9.81f };
		units = new String[] { "m/s²", "m/s²", "m/s²" };
	}

	/**
	 * Get Unit for given Dimension
	 * 
	 * @param myDimension
	 * @return Unit
	 */
	public String getUnit(int myDimension) {
		return units[myDimension];
	}

	/**
	 * Sets complete set of Calibration Data.
	 * 
	 * @param zeros
	 *            Raw Wii Values for Zero-Postion
	 * @param valueInts
	 *            Raw Wii Values for Value-Position
	 * @param valueFloats
	 *            Values represented by valueInts
	 * @param myUnits
	 *            Units
	 */
	public void setCalibration(int[] zeros, int[] valueInts, float[] valueFloats, String[] myUnits) {
		calibrateZeros = zeros;
		calibrateValues = valueInts;
		calibrateRepresentValues = valueFloats;
		units = myUnits;
		generateConfigElement();
		settings.setMoteCalibration(configElement, bluetoothAddress);
		settings.save();
	}

	/**
	 * Changes current Calibration to one read by XML-File.
	 * 
	 * @param config
	 *            the mote-Element from XML-File
	 */
	@SuppressWarnings("unchecked")
	private void setCalibration(Element config) {
		List<Element> cfgCZeros = config.getChild("calibratezeros").getChildren();
		List<Element> cfgCValues = config.getChild("calibrateValues").getChildren();
		List<Element> cfgRValues = config.getChild("calibrateRepresentValues").getChildren();
		List<Element> cfgUnits = config.getChild("units").getChildren();

		int zeros[] = { Integer.parseInt(cfgCZeros.get(0).getValue()), Integer.parseInt(cfgCZeros.get(1).getValue()), Integer.parseInt(cfgCZeros.get(2).getValue()) };
		int valueInts[] = { Integer.parseInt(cfgCValues.get(0).getValue()), Integer.parseInt(cfgCValues.get(1).getValue()), Integer.parseInt(cfgCValues.get(2).getValue()) };
		float valueFloats[] = { Float.parseFloat(cfgRValues.get(0).getValue()), Float.parseFloat(cfgRValues.get(1).getValue()), Float.parseFloat(cfgRValues.get(2).getValue()) };
		String myUnits[] = { cfgUnits.get(0).getValue(), cfgUnits.get(1).getValue(), cfgUnits.get(2).getValue() };

		setCalibration(zeros, valueInts, valueFloats, myUnits);

	}

	/**
	 * Returns Values after calculating calibration
	 * 
	 * @param myDimension
	 *            Dimension
	 * @param value
	 *            Raw Wii Value
	 * @return
	 */
	public float getCalibratedValue(int myDimension, int value) {
		return calibrateRepresentValues[myDimension] * (((float) value - calibrateZeros[myDimension]) / calibrateValues[myDimension]);
	}

	/**
	 * Returns Values after calculating calibration
	 * 
	 * @param myDimension
	 *            Dimension
	 * @param value
	 *            Raw Wii Value (Round)
	 * @return
	 */
	public float getCalibratedValue(int myDimension, double value) {
		return calibrateRepresentValues[myDimension] * (((float) value - calibrateZeros[myDimension]) / calibrateValues[myDimension]);
	}

	/**
	 * get the Zero Raw-Data Value for given Dimension
	 * 
	 * @param myDimension
	 * @return
	 */
	public int getZeros(int myDimension) {
		return calibrateZeros[myDimension];
	}

	/**
	 * get the 1g Raw-Data Value for Fiven Dimension
	 * 
	 * @param myDimension
	 * @return
	 */
	public int getGravity(int myDimension) {
		return calibrateValues[myDimension];
	}

	/**
	 * generates an Element for XML-Settings-file.
	 * 
	 * @return
	 */
	public Element getConfig() {
		return configElement;
	}

	/**
	 * Generate XML-Config from existing calibration Data. (i.e. after
	 * Calibrating)
	 * 
	 */
	private void generateConfigElement() {
		Element cfgCZeros = new Element("calibratezeros");
		for (int i = 0; i < calibrateZeros.length; i++) {
			cfgCZeros.addContent(new Element("dim").setAttribute("id", "" + i).setText("" + calibrateZeros[i]));
		}

		Element cfgCValues = new Element("calibrateValues");
		for (int i = 0; i < calibrateValues.length; i++) {
			cfgCValues.addContent(new Element("dim").setAttribute("id", "" + i).setText("" + calibrateValues[i]));
		}

		Element cfgRValues = new Element("calibrateRepresentValues");
		for (int i = 0; i < calibrateRepresentValues.length; i++) {
			cfgRValues.addContent(new Element("dim").setAttribute("id", "" + i).setText("" + calibrateRepresentValues[i]));
		}

		Element cfgUnits = new Element("units");
		for (int i = 0; i < units.length; i++) {
			cfgUnits.addContent(new Element("dim").setAttribute("id", "" + i).setText("" + units[i]));
		}

		configElement = new Element("mote").addContent(cfgCZeros).addContent(cfgCValues).addContent(cfgRValues).addContent(cfgUnits);
	}

	public void loadFromConfig(String mybluetoothAddress) {
		bluetoothAddress = mybluetoothAddress;
		configElement = settings.getMoteCalibrationElement(bluetoothAddress);
		if (configElement == null) {
			generateConfigElement();
			settings.setMoteCalibration(configElement, bluetoothAddress);
			settings.save();
		} else {
			setCalibration(configElement);
		}

	}

	public int getDimensions() {
		return dimensions;
	}

	public Float getCalibrateRepresentValue(int myDimension) {
		return calibrateRepresentValues[myDimension];
	}

}
