package org.phymote.gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.phymote.PhyMote;
import org.phymote.control.exceptions.MergeDimensionUnitNotMatchException;
import org.phymote.control.exceptions.Messages;
import org.phymote.control.listener.MouseClickListener;
import org.phymote.sensor.ChartModificationSensor;
import org.phymote.sensor.PhSensor;

public class VirtualSensorChartInternalFrame extends ChartInternalFrame {
	private static final long serialVersionUID = 1L;
	private ChartModificationSensor sensor;
	private boolean visibleDimensions[];

	public VirtualSensorChartInternalFrame(PhyMote myPhMote, String myTitle, ChartModificationSensor mySensor) {
		super(myPhMote, myTitle, mySensor);
		sensor = mySensor;
		initAdditionalButtons();

		visibleDimensions = new boolean[mySensor.getActiveDataRow().getDimension()];
		for (int i = 0; i < visibleDimensions.length; i++) {
			visibleDimensions[i] = true;
		}
		
		//zoom out to maximum:
		getChartPanel().getChart().getXYPlot().getDomainAxis().setAutoRange(true);
		getChartPanel().getChart().getXYPlot().getRangeAxis().setAutoRange(true);
	}

	@Override
	public PhSensor getSensor() {
		return sensor;
	}

	private void initAdditionalButtons() {
		JLabel buttonDimX = new JLabel();
		buttonDimX.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorChartInternalFrame.icon_select_dimension")))); //$NON-NLS-1$
		buttonDimX.setName("buttonDimensions");
		buttonDimX.setToolTipText(Messages.getString("VirtualSensorChartInternalFrame.tooltip_select_data")); //$NON-NLS-1$
		buttonDimX.setPreferredSize(new Dimension(32, 32));
		buttonDimX.addMouseListener(new MouseClickListener(buttonDimX) {
			public void mousePressed(MouseEvent arg0) {
				DataRowSelectDialog dataRowSelect = new DataRowSelectDialog(rootPhyMote.getMainFrame(), sensor.getActiveDataRow());
				if (dataRowSelect.hasResult()) {
					visibleDimensions = dataRowSelect.getResult();
					chartPanel.setChart(sensor.getActiveDataRow().getChart(visibleDimensions));
					chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
					chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
				}
			};
		});

		JLabel buttonSmoothen = new JLabel();
		buttonSmoothen.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorChartInternalFrame.file_icon_smoothen")))); //$NON-NLS-1$
		buttonSmoothen.setName("buttonSmoothen");
		buttonSmoothen.setToolTipText(Messages.getString("VirtualSensorChartInternalFrame.tooltip_smoothen")); //$NON-NLS-1$
		buttonSmoothen.setPreferredSize(new Dimension(32, 32));
		buttonSmoothen.addMouseListener(new MouseClickListener(buttonSmoothen) {
			public void mousePressed(MouseEvent arg0) {
				sensor.setUndoPoint();

				SmoothenSelectDialog smoothenSelect = new SmoothenSelectDialog(rootPhyMote.getMainFrame());
				if (smoothenSelect.hasResult()) {
					sensor.getActiveDataRow().smoothen(smoothenSelect.getResult());
					// System.out.println("Length: "+smoothenSelect.getResult().getLength());
					// System.out.println("Orientation "+smoothenSelect.getResult().getOrientation());
				}

				chartPanel.setChart(sensor.getActiveDataRow().getChart(visibleDimensions));
				chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
				chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
			};
		});

		JLabel buttonInvert = new JLabel();
		buttonInvert.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorChartInternalFrame.file_icon_invert")))); //$NON-NLS-1$
		buttonInvert.setName("buttonInvert");
		buttonInvert.setToolTipText(Messages.getString("VirtualSensorChartInternalFrame.tooltip_invert")); //$NON-NLS-1$
		buttonInvert.setPreferredSize(new Dimension(32, 32));
		buttonInvert.addMouseListener(new MouseClickListener(buttonInvert) {
			public void mousePressed(MouseEvent arg0) {
				sensor.setUndoPoint();
				sensor.getActiveDataRow().invert();
				chartPanel.setChart(sensor.getActiveDataRow().getChart(visibleDimensions));
				chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
				chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
			};
		});

		JLabel buttonMerge = new JLabel();
		buttonMerge.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorChartInternalFrame.file_icon_merge")))); //$NON-NLS-1$
		buttonMerge.setName("buttonMerge");
		buttonMerge.setToolTipText(Messages.getString("VirtualSensorChartInternalFrame.tooltip_merge")); //$NON-NLS-1$
		buttonMerge.setPreferredSize(new Dimension(32, 32));
		buttonMerge.addMouseListener(new MouseClickListener(buttonMerge) {
			public void mousePressed(MouseEvent arg0) {
				try {
					sensor.setUndoPoint();
					sensor.getActiveDataRow().mergeDimensions(visibleDimensions);

					// reset visible dimensions
					visibleDimensions = new boolean[sensor.getActiveDataRow().getDimension()];
					visibleDimensions[0] = true;

					chartPanel.setChart(sensor.getActiveDataRow().getChart(visibleDimensions));
					chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
					chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
				} catch (MergeDimensionUnitNotMatchException e) {
					new ReallySureDialog(rootPhyMote.getMainFrame(), Messages.getString("VirtualSensorChartInternalFrame.merge_unit_error"));
				}
			};
		});

		JLabel buttonIntegrate = new JLabel();
		buttonIntegrate.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorChartInternalFrame.file_icon_integrate")))); //$NON-NLS-1$
		buttonIntegrate.setName("buttonIntegrate");
		buttonIntegrate.setToolTipText(Messages.getString("VirtualSensorChartInternalFrame.tooltip_integrate")); //$NON-NLS-1$
		buttonIntegrate.setPreferredSize(new Dimension(32, 32));
		buttonIntegrate.addMouseListener(new MouseClickListener(buttonIntegrate) {
			public void mousePressed(MouseEvent arg0) {
				sensor.setUndoPoint();
				for (int i = 0; i < visibleDimensions.length; i++) {
					if (visibleDimensions[i]) {
						sensor.getActiveDataRow().integrate(i);
					}
				}
				chartPanel.setChart(sensor.getActiveDataRow().getChart(visibleDimensions));
				chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
				chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
			};
		});

		JLabel buttonCrop = new JLabel();
		buttonCrop.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorChartInternalFrame.file_icon_crop")))); //$NON-NLS-1$
		buttonCrop.setName("buttonCrop");
		buttonCrop.setToolTipText(Messages.getString("VirtualSensorChartInternalFrame.tooltip_crop")); //$NON-NLS-1$
		buttonCrop.setPreferredSize(new Dimension(32, 32));
		buttonCrop.addMouseListener(new MouseClickListener(buttonCrop) {
			public void mousePressed(MouseEvent e) {
				if (chartPanel.getDomainMarker() != null) {
					sensor.setUndoPoint();
					sensor.getActiveDataRow().cutTime(chartPanel.getDomainMarker());
					chartPanel.setChart(sensor.getActiveDataRow().getChart(visibleDimensions));
					chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
					chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
				} else {
					JOptionPane.showMessageDialog(rootPane, Messages.getString("VirtualSensorChartInternalFrame.tooltip_mark_range")); //$NON-NLS-1$
					// TODO: besser machen!
				}
			}
		});

		JLabel buttonZeroize = new JLabel();
		buttonZeroize.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorChartInternalFrame.file_icon_integrate_zero")))); //$NON-NLS-1$
		buttonZeroize.setName("buttonZeroize");
		buttonZeroize.setToolTipText(Messages.getString("VirtualSensorChartInternalFrame.tooltip_reset_zero")); //$NON-NLS-1$
		buttonZeroize.setPreferredSize(new Dimension(32, 32));
		buttonZeroize.addMouseListener(new MouseClickListener(buttonZeroize) {
			public void mousePressed(MouseEvent e) {
				if (chartPanel.getDomainMarker() != null) {
					sensor.setUndoPoint();
					sensor.getActiveDataRow().calibrateZeros(visibleDimensions, chartPanel.getDomainMarker());
					chartPanel.setChart(sensor.getActiveDataRow().getChart(visibleDimensions));
					chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
					chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
				} else {
					JOptionPane.showMessageDialog(rootPane, Messages.getString("VirtualSensorChartInternalFrame.tooltip_mark_range")); //$NON-NLS-1$
					// TODO: besser machen!
				}
			}
		});

		JLabel buttonUndo = new JLabel();
		buttonUndo.setIcon(new ImageIcon(getClass().getResource(Messages.getString("VirtualSensorChartInternalFrame.file_icon_undo")))); //$NON-NLS-1$
		buttonUndo.setName("buttonUndo");
		buttonUndo.setToolTipText(Messages.getString("VirtualSensorChartInternalFrame.tooltip_undo")); //$NON-NLS-1$
		buttonUndo.setPreferredSize(new Dimension(32, 32));
		buttonUndo.addMouseListener(new MouseClickListener(buttonUndo) {
			public void mousePressed(MouseEvent e) {
				sensor.undo();
				chartPanel.setChart(sensor.getActiveDataRow().getChart(visibleDimensions));
				chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
				chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
			}
		});

		buttonLine.add(buttonUndo);
		buttonLine.add(buttonDimX);
		buttonLine.add(buttonSmoothen);
		buttonLine.add(buttonInvert);
		buttonLine.add(buttonMerge);
		buttonLine.add(buttonIntegrate);
		buttonLine.add(buttonZeroize);
		buttonLine.add(buttonCrop);
	}

	public int getVisibleDimensionsCount() {
		int i = 0;
		for (boolean dim : visibleDimensions) {
			if (dim) {
				i++;
			}
		}
		return i;
	}

}
