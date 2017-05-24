package org.phymote.persistance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Settings class for main program settings and calibration data
 * 
 */
public class Settings {
	private File settingsFile;
	private Document sDoc;
	private final String SETTINGSFILENAME = "phymote-settings.xml";
	private final String SETTINGSVERSION = "0.1";

	public Settings() {
		settingsFile = new File(SETTINGSFILENAME);
		// this should also check if settings version differs from program's.
		if (settingsFile.exists()) {
			loadSettings();
		} else {
			createNew();
		}
	}

	/**
	 * Saves current Settings.
	 * 
	 */
	public void save() {
		try {
			new XMLOutputter().output(sDoc, new FileWriter(settingsFile));
		} catch (IOException e) {
			System.err.println("Error writing settings file.");
		}
	}

	/**
	 * Opens existing Settings file.
	 * 
	 */
	private void loadSettings() {
		try {
			sDoc = new SAXBuilder().build(settingsFile);
		} catch (JDOMException e) {
			System.err.println("Error reading settings file.");
			createNew(); // better than nothing.
		} catch (IOException e) {
			System.err.println("Error reading settings file.");
			createNew(); // better than nothing.
		}
	}

	/**
	 * Creates new Plain settings xml file.
	 * 
	 */
	private void createNew() {
		sDoc = new Document(new Element("phymote"));
		sDoc.getRootElement().setAttribute("version", SETTINGSVERSION);
		sDoc.getRootElement().addContent(new Element("motes"));
		sDoc.getRootElement().addContent(new Element("settings"));
		try {
			new XMLOutputter().output(sDoc, new FileWriter(settingsFile));
		} catch (IOException e) {
			System.err.println("Error writing settings file.");
		}
	}

	@SuppressWarnings("unchecked")
	public Element getMoteCalibrationElement(String bluetoothAddress) {
		List<Element> motes = sDoc.getRootElement().getChild("motes").getChildren("mote");
		for (int i = 0; i < motes.size(); i++) {
			try {
				if (motes.get(i).getAttribute("id").getValue().equalsIgnoreCase(bluetoothAddress)) {
					return motes.get(i);
				}
			} catch (Exception e) {
				// perhaps better create a new settings file.
				createNew();
			}
		}
		return null;
	}

	public void setMoteCalibration(Element configElement, String bluetoothAddress) {
		Element existingElement = getMoteCalibrationElement(bluetoothAddress);
		if (existingElement != null) {
			existingElement.getParentElement().addContent(configElement.setAttribute("id", bluetoothAddress));
			existingElement.detach();
		} else {
			configElement.setAttribute("id", bluetoothAddress);
			sDoc.getRootElement().getChild("motes").addContent(configElement);
		}
	}

}
