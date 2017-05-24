package org.phymote.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.data.Range;
import org.jfree.ui.RectangleEdge;
import org.phymote.PhyMote;
import org.phymote.control.PhyMoteDataFileFilter;
import org.phymote.control.exceptions.Messages;
import org.phymote.control.listener.ChartRangeMouseListener;
import org.phymote.control.listener.MouseClickListener;
import org.phymote.control.listener.RangeClickedEvent;
import org.phymote.control.listener.RangeClickedListener;
import org.phymote.gui.layout.ExtendedFlowLayout;
import org.phymote.sensor.ChartModificationSensor;
import org.phymote.sensor.PhSensor;

/**
 * @author Christoph Krichenbauer IntenalFrame for displaying graphs for real
 *         Sensors. Possibility to see graph while capturing
 */
public class ChartInternalFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	protected SensorChartPanel chartPanel;
	protected PhyMote rootPhyMote;
	private ChartInternalFrame myself = this;
	private PhSensor sensor;

	protected JPanel buttonLine;

	private JLabel buttonLink;
	protected boolean linkedZoom = true;
	private JLabel buttonEdit;
	private JLabel buttonSave;
	private JLabel statusbar;

	/**
	 * Main Constructor.
	 * 
	 * @param myPhyMote
	 *            main PhyMoteObject
	 * @param myTitle
	 *            displayed title
	 * @param mySensor
	 *            (real) Sensor to display data for.
	 */
	public ChartInternalFrame(PhyMote myPhyMote, String myTitle, PhSensor mySensor) {
		super();
		rootPhyMote = myPhyMote;
		sensor = mySensor;
		chartPanel = new SensorChartPanel(mySensor.getActiveDataRow().getChart());

		chartPanel.getChart().getXYPlot().getDomainAxis().setRange(new Range(0.0, 1000));
		chartPanel.getChart().getXYPlot().getRangeAxis().setRange(new Range(-80.0, 80.0));
		chartPanel.getChart().setAntiAlias(false);

		chartPanel.getChart().getXYPlot().setBackgroundPaint(Color.WHITE);
		chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.GRAY);
		chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.GRAY);

		chartPanel.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
			}

			public void mouseMoved(MouseEvent e) {
				if (chartPanel.getAxisTrace()) {
					setStatusBarText(Messages.getString("ChartInternalFrame.time") + " = " //$NON-NLS-1$
							+ (Math.round(chartPanel.getChart().getXYPlot().getDomainAxis()
									.java2DToValue(e.getPoint().getX(), chartPanel.getScreenDataArea(), RectangleEdge.BOTTOM)) / 1000.0f)
							+ "s; " + Messages.getString("ChartInternalFrame.value") + " = " //$NON-NLS-2$ //$NON-NLS-3$
							+ (Math
									.round(chartPanel.getChart().getXYPlot().getRangeAxis().java2DToValue(e.getPoint().getY(), chartPanel.getScreenDataArea(), RectangleEdge.LEFT) * 100.0))
							/ 100.0f);
				}
			}
		});

		setResizable(true);
		setTitle(myTitle);
		setMaximizable(true);
		setClosable(true);
		setSize(new Dimension(640, 480));
		setPreferredSize(new Dimension(640, 480));
		setVisible(true);
		setContentPane(new JPanel());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(chartPanel, BorderLayout.CENTER);
		initButtons();
		initStatusbar();

		// add linked zoom listeners...
		chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
		chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());

		addInternalFrameListener(new InternalFrameListener() {
			public void internalFrameClosed(InternalFrameEvent e) {
				rootPhyMote.removeChartFrame(myself);
			}

			public void internalFrameActivated(InternalFrameEvent arg0) {
			}

			public void internalFrameClosing(InternalFrameEvent arg0) {
			}

			public void internalFrameDeactivated(InternalFrameEvent arg0) {
			}

			public void internalFrameDeiconified(InternalFrameEvent arg0) {
			}

			public void internalFrameIconified(InternalFrameEvent arg0) {
			}

			public void internalFrameOpened(InternalFrameEvent arg0) {
			}
		});

		ChartRangeMouseListener crml = new ChartRangeMouseListener(chartPanel);
		crml.addRangeClickedListener(new RangeClickedListener() {
			public void removeRange() {
				chartPanel.setRangeMarker(null);
				chartPanel.setDomainMarker(null);
			}

			public void rangeClicked(RangeClickedEvent e) {
				chartPanel.setRangeMarker(new IntervalMarker(e.getLowY(), e.getHighY()));
				chartPanel.setDomainMarker(new IntervalMarker(e.getLowX(), e.getHighX()));
				setStatusBarText(Messages.getString("ChartInternalFrame.deltaTime") + " : " //$NON-NLS-1$
						+ ((Math.round(chartPanel.getDomainMarker().getEndValue() - chartPanel.getDomainMarker().getStartValue())) / 1000.0f)
						+ "s; " + Messages.getString("ChartInternalFrame.deltaValue") + ": " //$NON-NLS-2$
						+ ((Math.round((chartPanel.getRangeMarker().getEndValue() - chartPanel.getRangeMarker().getStartValue()) * 100.0f)) / 100.0f));
			}
		});
		chartPanel.addMouseListener(crml);
	}

	private void initStatusbar() {
		statusbar = new JLabel(" ", SwingConstants.LEFT); //$NON-NLS-1$
		statusbar.setSize(320, 10);
		statusbar.setVisible(true);
		statusbar.setBackground(Color.DARK_GRAY);
		getContentPane().add(statusbar, BorderLayout.SOUTH);
	}

	/**
	 * Change statusbartext.
	 * 
	 * @param text
	 *            new text for statusbar
	 */
	public void setStatusBarText(String text) {
		if (text.equals(Messages.getString("ChartInternalFrame.8")) || (text == null)) { //$NON-NLS-1$
			text = Messages.getString("ChartInternalFrame.9"); //$NON-NLS-1$
		}
		statusbar.setText(text);
	}

	/**
	 * @return linked ChartPanel (generated for this frame)
	 */
	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	/**
	 * Triggers update of zoom-settings. Will automaticly generate Values.
	 */
	public void updateZoom() {
		if (rootPhyMote.isCaptureing() && !sensor.isVirtualSensor()) {
			chartPanel.getChart().getXYPlot().getDomainAxis().setRange(new Range(sensor.getActiveDataRow().getLastMTime() - 4000, sensor.getActiveDataRow().getLastMTime() + 6000));
			chartPanel.getChart().getXYPlot().getRangeAxis().setRange(new Range(-50.0, 50.0));
		} else {
			chartPanel.getChart().getXYPlot().getRangeAxis().setAutoRange(true);
			chartPanel.getChart().getXYPlot().getDomainAxis().setAutoRange(true);
		}
	}

	/**
	 * Update domainaxis-zoom to a given range.
	 * 
	 * @param range
	 */
	public void updateDomainZoom(Range range) {
		if ((range != chartPanel.getChart().getXYPlot().getDomainAxis().getRange()) && linkedZoom) {
			chartPanel.getChart().getXYPlot().getDomainAxis().setAutoRange(false);
			chartPanel.getChart().getXYPlot().getDomainAxis().setRange(range);
		}
	}

	/**
	 * Update valueaxis-zoom to a given range.
	 * 
	 * @param range
	 */
	public void updateValueZoom(Range range) {
		if ((range != chartPanel.getChart().getXYPlot().getRangeAxis().getRange()) && linkedZoom) {
			chartPanel.getChart().getXYPlot().getRangeAxis().setAutoRange(false);
			chartPanel.getChart().getXYPlot().getRangeAxis().setRange(range);
		}
	}

	/**
	 * @return used (real) sensor.
	 */
	public PhSensor getSensor() {
		return sensor;
	}

	/**
	 * triggers chart to be regenerated if a new datarow is set.
	 */
	public void informNewDataRow() {
		chartPanel.setChart(sensor.getActiveDataRow().getChart());
		chartPanel.getChart().getXYPlot().getDomainAxis().addChangeListener(rootPhyMote.getLinkedZoomDomainListener());
		chartPanel.getChart().getXYPlot().getRangeAxis().addChangeListener(rootPhyMote.getLinkedZoomValueListener());
		chartPanel.getChart().getXYPlot().setBackgroundPaint(Color.WHITE);
		chartPanel.getChart().getXYPlot().setDomainGridlinePaint(Color.GRAY);
		chartPanel.getChart().getXYPlot().setRangeGridlinePaint(Color.GRAY);
		updateZoom();
	}

	/**
	 * Set a new sensor.
	 * 
	 * @param mySensor
	 *            new Sensor.
	 */
	public void setSensor(PhSensor mySensor) {
		sensor = mySensor;
		informNewDataRow();
	}

	private void initButtons() {
		final ImageIcon chainLinkedIcon = new ImageIcon(getClass().getResource(Messages.getString("ChartInternalFrame.file_icon_link_chained"))); //$NON-NLS-1$
		final ImageIcon chainUnlinkedIcon = new ImageIcon(getClass().getResource(Messages.getString("ChartInternalFrame.file_icon_unlink_chained"))); //$NON-NLS-1$
		// buttonLink
		buttonLink = new JLabel();
		buttonLink.setText(Messages.getString("ChartInternalFrame.10")); //$NON-NLS-1$
		buttonLink.setIcon(chainLinkedIcon);
		buttonLink.setName(Messages.getString("ChartInternalFrame.7")); //$NON-NLS-1$
		buttonLink.setToolTipText(Messages.getString("ChartInternalFrame.file_link_zoom")); //$NON-NLS-1$
		buttonLink.setPreferredSize(new Dimension(32, 32));
		buttonLink.addMouseListener(new MouseClickListener(buttonLink) {
			public void mousePressed(MouseEvent arg0) {
				if (buttonLink.getIcon().equals(chainLinkedIcon)) {
					linkedZoom = false;
					buttonLink.setIcon(chainUnlinkedIcon);
				} else {
					linkedZoom = true;
					buttonLink.setIcon(chainLinkedIcon);
				}
			}
		});

		buttonEdit = new JLabel();
		buttonEdit.setIcon(new ImageIcon(getClass().getResource(Messages.getString("ChartInternalFrame.file_icon_edit")))); //$NON-NLS-1$
		buttonEdit.setName(Messages.getString("ChartInternalFrame.11")); //$NON-NLS-1$
		buttonEdit.setToolTipText(Messages.getString("ChartInternalFrame.tooltip_duplicate_data")); //$NON-NLS-1$
		buttonEdit.setPreferredSize(new Dimension(32, 32));
		buttonEdit.addMouseListener(new MouseClickListener(buttonEdit) {
			public void mousePressed(MouseEvent arg0) {
				rootPhyMote.addVirtualChartFrame(new ChartModificationSensor(sensor));
			};
		});

		buttonSave = new JLabel();
		buttonSave.setIcon(new ImageIcon(getClass().getResource(Messages.getString("ChartInternalFrame.file_icon_save")))); //$NON-NLS-1$
		buttonSave.setToolTipText(Messages.getString("ChartInternalFrame.tooltip_save")); //$NON-NLS-1$
		buttonSave.setName(Messages.getString("ChartInternalFrame.12")); //$NON-NLS-1$
		buttonSave.setPreferredSize(new Dimension(32, 32));
		buttonSave.addMouseListener(new MouseClickListener(buttonSave) {
			public void mousePressed(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new PhyMoteDataFileFilter());

				if (fc.showSaveDialog(myself) == JFileChooser.APPROVE_OPTION) {
					File saveFile = fc.getSelectedFile();
					if (!PhyMoteDataFileFilter.getExtension(saveFile).equals(PhyMoteDataFileFilter.phyMoteSaveFileExtension)) {
						saveFile = new File(saveFile.getPath() + Messages.getString("ChartInternalFrame.13") //$NON-NLS-1$
								+ PhyMoteDataFileFilter.phyMoteSaveFileExtension);
					}
					new ChartModificationSensor(sensor, false).saveXML(saveFile);
				}
			};
		});

		JLabel buttonCrosshair = new JLabel();
		buttonCrosshair.setIcon(new ImageIcon(getClass().getResource(Messages.getString("ChartInternalFrame.file_icon_crosshair")))); //$NON-NLS-1$
		buttonCrosshair.setToolTipText(Messages.getString("ChartInternalFrame.tooltip_toggle_crosshair")); //$NON-NLS-1$
		buttonCrosshair.setName(Messages.getString("ChartInternalFrame.14")); //$NON-NLS-1$
		buttonCrosshair.setPreferredSize(new Dimension(32, 32));
		buttonCrosshair.addMouseListener(new MouseClickListener(buttonCrosshair) {
			public void mousePressed(MouseEvent arg0) {
				chartPanel.enableAxisTrace();
			};
		});

		JLabel buttonSCS = new JLabel();
		buttonSCS.setIcon(new ImageIcon(getClass().getResource(Messages.getString("ChartInternalFrame.file_icon_export")))); //$NON-NLS-1$
		buttonSCS.setToolTipText(Messages.getString("ChartInternalFrame.tooltip_copy_SCS")); //$NON-NLS-1$
		buttonSCS.setName(Messages.getString("ChartInternalFrame.15")); //$NON-NLS-1$
		buttonSCS.setPreferredSize(new Dimension(32, 32));
		buttonSCS.addMouseListener(new MouseClickListener(buttonSCS) {
			public void mousePressed(MouseEvent arg0) {
				new CopyPasteDialog(rootPhyMote.getMainFrame(), getSensor().getActiveDataRow().getSCString());
			};
		});

		buttonLine = new JPanel();
		buttonLine.setName(Messages.getString("ChartInternalFrame.16")); //$NON-NLS-1$
		getContentPane().add(buttonLine, BorderLayout.NORTH);
		buttonLine.setBackground(Color.WHITE);
		buttonLine.setLayout(new ExtendedFlowLayout(FlowLayout.LEFT, 0, 0));

		buttonLine.add(buttonSave);
		buttonLine.add(buttonSCS);
		buttonLine.add(buttonEdit);
		buttonLine.add(buttonLink);
		buttonLine.add(buttonCrosshair);

	}

}
