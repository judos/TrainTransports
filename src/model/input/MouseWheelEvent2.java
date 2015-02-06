package model.input;

import java.awt.event.MouseWheelEvent;

/**
 * @since 01.02.2015
 * @author Julian Schelker
 */
public class MouseWheelEvent2 {

	protected int		scrollAmount;
	protected double	rotation;

	public MouseWheelEvent2(MouseWheelEvent e) {
		this.scrollAmount = e.getScrollAmount();
		this.rotation = e.getPreciseWheelRotation();
	}

	/**
	 * @return the scrollAmount
	 */
	public int getScrollAmount() {
		return scrollAmount;
	}

	/**
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}

}
