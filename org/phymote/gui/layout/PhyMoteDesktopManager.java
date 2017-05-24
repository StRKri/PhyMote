package org.phymote.gui.layout;

import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class PhyMoteDesktopManager extends DefaultDesktopManager implements DesktopManager {

	JDesktopPane desktopPane;
	private static final int BORDERLEFT = 10;
	private static final int BORDERRIGHT = 10;
	private static final int BORDERTOP = 20;
	private static final int BORDERBOTTOM = 10;

	private static final long serialVersionUID = -8869840742949052972L;

	public PhyMoteDesktopManager(JDesktopPane myDesktopPane) {
		super();
		desktopPane = myDesktopPane;
	}

	@Override
	public void dragFrame(JComponent f, int newX, int newY) {
		if (newX < PhyMoteDesktopManager.BORDERLEFT) {
			newX = PhyMoteDesktopManager.BORDERLEFT;
		}
		if (newX > desktopPane.getWidth() - PhyMoteDesktopManager.BORDERRIGHT - f.getWidth()) {
			newX = desktopPane.getWidth() - PhyMoteDesktopManager.BORDERRIGHT - f.getWidth();
		}

		if (newY < PhyMoteDesktopManager.BORDERTOP) {
			newY = PhyMoteDesktopManager.BORDERTOP;
		}
		if (newY > desktopPane.getHeight() - PhyMoteDesktopManager.BORDERBOTTOM - f.getHeight()) {
			newY = desktopPane.getHeight() - PhyMoteDesktopManager.BORDERBOTTOM - f.getHeight();
		}

		super.dragFrame(f, newX, newY);
	}

	@Override
	public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {

		if (newX < PhyMoteDesktopManager.BORDERLEFT) {
			newX = PhyMoteDesktopManager.BORDERLEFT;
		}
		if (newX > desktopPane.getWidth() - PhyMoteDesktopManager.BORDERRIGHT - f.getWidth()) {
			newX = desktopPane.getWidth() - PhyMoteDesktopManager.BORDERRIGHT - f.getWidth();
		}

		if (newY < PhyMoteDesktopManager.BORDERTOP) {
			newY = PhyMoteDesktopManager.BORDERTOP;
		}
		if (newY > desktopPane.getHeight() - PhyMoteDesktopManager.BORDERBOTTOM - f.getHeight()) {
			newY = desktopPane.getHeight() - PhyMoteDesktopManager.BORDERBOTTOM - f.getHeight();
		}

		super.setBoundsForFrame(f, newX, newY, newWidth, newHeight);
	}
	

}
