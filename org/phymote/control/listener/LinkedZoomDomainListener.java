package org.phymote.control.listener;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.AxisChangeEvent;
import org.jfree.chart.event.AxisChangeListener;
import org.phymote.PhyMote;

/**
 * @author Christoph Krichenbauer Listener used to zoom all charts together.
 * 
 */
public class LinkedZoomDomainListener implements AxisChangeListener {

	private PhyMote mainPhyMote;

	public LinkedZoomDomainListener(PhyMote myPhyMote) {
		mainPhyMote = myPhyMote;
	}

	public void axisChanged(AxisChangeEvent event) {
		if (!mainPhyMote.isCaptureing()) {
			mainPhyMote.linkChartFramesDomainZoom(((NumberAxis) event.getAxis()).getRange());
		}
	}

}