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

	public static PointI getMousePoint() {
		Point r = componentX.getMousePosition();
		if (r != null)
			return new PointI(r);
		return new PointI(-1, -1);
	}

	public static PointI getMouseMapPoint() {
		Point p = componentX.getMousePosition();
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
}
