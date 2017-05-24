package org.phymote.control.listener;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.phymote.gui.SensorMenu;

/**
 * @author Christoph Krichenbauer Simple listener to just carry about the mouse
 *         clicks, not the movements.
 */
public abstract class MouseClickListener implements MouseListener {
	protected SensorMenu menu;

	public MouseClickListener() {
		super();
	}

	public MouseClickListener(SensorMenu myMenu) {
		this();
		menu = myMenu;
	}

	public MouseClickListener(Component myComponent) {
		this();
		myComponent.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}
