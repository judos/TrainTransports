package model;

import java.awt.Component;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import model.map.Scroll;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class Mouse {
	private static Component	componentX;
	private static Scroll		scrollX;

	public static Point getMousePoint() {
		Point r = componentX.getMousePosition();
		if (r != null)
			return r;
		return new Point(-1, -1);
	}

	public static Point getMouseMapPoint() {
		Point p = componentX.getMousePosition();
		if (p == null)
			return null;
		AffineTransform t = scrollX.getTransformScreenToMap();
		Point2D r = t.transform(p, null);
		return new Point((int) r.getX(), (int) r.getY());
	}

	public static void initialize(Scroll scroll, Component component) {
		scrollX = scroll;
		componentX = component;
	}
}
