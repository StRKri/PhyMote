package org.phymote.sensor;

public abstract class NameablePhSensor implements PhSensor {

	private String name;
	private String[] descDimension;

	public void setDesc(String[] myDesc) {
		descDimension = myDesc.clone();
	}

	public void setName(String myName) {
		name = myName;
	}

	public String getName() {
		return name;
	}

	public String getDesc(int myDimension) {
		try {
			return descDimension[myDimension];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public void setDesc(int myDimension, String desc) {
		try {
			descDimension[myDimension] = desc;
		} catch (ArrayIndexOutOfBoundsException e) {
			// do nothing.
		}
	}

	public String[] getDescArray() {
		return descDimension.clone();
	}
}
