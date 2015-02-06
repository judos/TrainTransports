package model.input;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class MouseEvent2 {

	protected InputType	type;
	protected Point		screenPosition;
	protected int		button;
	protected Point		mapPosition;

	public MouseEvent2(InputType type, MouseEvent event, Point onMap) {
		this.type = type;
		this.screenPosition = event.getPoint();
		this.mapPosition = onMap;
		this.button = event.getButton();
	}

	/**
	 * @return the mapPosition
	 */
	public Point getMapPosition() {
		return mapPosition;
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
