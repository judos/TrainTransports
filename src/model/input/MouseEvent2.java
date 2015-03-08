package model.input;

import java.awt.event.MouseEvent;

import ch.judos.generic.data.geometry.PointI;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class MouseEvent2 {

	protected InputType	type;
	protected PointI	screenPosition;
	protected int		button;
	protected PointI	mapPosition;

	public MouseEvent2(InputType type, MouseEvent event, PointI onMap) {
		this(type, new PointI(event.getPoint()), onMap, event.getButton());
	}

	protected MouseEvent2(InputType type, PointI screen, PointI onMap, int button) {
		this.type = type;
		this.screenPosition = screen;
		this.mapPosition = onMap;
		this.button = button;
	}

	/**
	 * @return the mapPosition
	 */
	public PointI getMapPosition() {
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
	public PointI getScreenPosition() {
		return this.screenPosition;
	}

	/**
	 * @return the button
	 */
	public int getButton() {
		return this.button;
	}

	public MouseEvent2 deepCopy() {
		return new MouseEvent2(type, this.screenPosition.deepCopy(), this.mapPosition
			.deepCopy(), this.button);
	}

}
