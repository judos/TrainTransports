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
		this(type, event.getPoint(), onMap, event.getButton());
	}

	protected MouseEvent2(InputType type, Point screen, Point onMap, int button) {
		this.type = type;
		this.screenPosition = screen;
		this.mapPosition = onMap;
		this.button = button;
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
	 * @return a copy of the position
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

	public MouseEvent2 deepCopy() {
		return new MouseEvent2(type, this.screenPosition.getLocation(), this.mapPosition
			.getLocation(), this.button);
	}

}
