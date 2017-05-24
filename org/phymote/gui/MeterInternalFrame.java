package org.phymote.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * Internal Frame for displaying live data in analog meters.
 * 
 * @author Christoph Krichenbauer
 * 
 */
public class MeterInternalFrame extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private ChartPanel chartPanel;

	public MeterInternalFrame(String myTitle, JFreeChart chart) {
		super();
		chartPanel = new ChartPanel(chart, false, false, false, false, false);
		chartPanel.setPreferredSize(new Dimension(250, 250));
		chartPanel.getChart().setAntiAlias(true); 
		
		setResizable(false);
		setTitle(myTitle);
		setMaximizable(false);
		setClosable(true);
		setSize(new Dimension(250, 250));
		setPreferredSize(new Dimension(250, 250));
		setContentPane(new JPanel());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(chartPanel, BorderLayout.CENTER);
		setVisible(true);
		setEnabled(true);
	}

}
