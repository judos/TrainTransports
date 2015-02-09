package model;

import java.awt.Component;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import model.map.Scroll;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class Mouse {
	private static Component	componentX;
	private static Scroll		scrollX;
	private static boolean		locked;
	private static Point		mp;

	private static Point mouse() {
		if (locked)
			return mp;
		return componentX.getMousePosition();
	}

	public static PointI getMousePoint() {
		Point r = mouse();
		if (r != null)
			return new PointI(r);
		return null;
	}

	public static PointI getMouseMapPoint() {
		Point p = mouse();
		if (p == null)
			return null;
		AffineTransform t = scrollX.getTransformScreenToMap();
		PointI result = new PointI();
		t.transform(p, result);
		return result;
	}

	public static void initialize(Scroll scroll, Component component) {
		scrollX = scroll;
		componentX = component;
	}

	public static void toggleLock() {
		locked = !locked;
		if (locked)
			mp = componentX.getMousePosition();
	}
}
