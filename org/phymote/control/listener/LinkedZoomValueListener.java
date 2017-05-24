package org.phymote.control.listener;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.phymote.PhyMote;

/**
 * @author Christoph Krichenbauer Listener used to zoom all charts together.
 * 
 */
public class LinkedZoomValueListener implements AxisChangeListener {

	private PhyMote mainPhyMote;

	public LinkedZoomValueListener(PhyMote myPhyMote) {
		mainPhyMote = myPhyMote;
	}

	public void axisChanged(AxisChangeEvent event) {
		if (!mainPhyMote.isCaptureing()) {
			mainPhyMote.linkChartFramesValueZoom(((NumberAxis) event.getAxis()).getRange());
		}
	}

}
