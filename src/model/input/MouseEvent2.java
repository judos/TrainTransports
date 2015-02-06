package model.input;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class MouseEvent2 {

	private InputType	type;

	private Point		screenPosition;
	private int			button;

	public MouseEvent2() {}

	public MouseEvent2(InputType t, MouseEvent e) {
		this.type = t;
		this.screenPosition = e.getPoint();
		this.button = e.getButton();
	}

	/**
	 * @return the type
	 */
	public InputType getType() {
		return this.type;
	}

	/**
	 * @return the position
	 */
	public Point getScreenPosition() {
		return this.screenPosition;
	}

	/**
	 * @return the button
	 */
	public int getButton() {
		return this.button;
	}

}
