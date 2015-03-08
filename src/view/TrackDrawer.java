package view;

import static model.objects.Track.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.graphics.ColorUtils;

/**
 * @since 08.03.2015
 * @author Julian Schelker
 */
public class TrackDrawer {

	public static void drawStraightFromTo(Graphics2D g, PointI start, PointI end,
		Color colorOver, int layer) {
		double length = start.distance(end);
		AffineTransform t = g.getTransform();
		g.translate(start.x, start.y);
		g.rotate(start.getAngleTo(end));

		if (layer == 0) {
			if (colorOver != null)
				g.setColor(ColorUtils.mix(bedColour, colorOver));
			else
				g.setColor(bedColour);

			int fits = (int) Math.round(length / sleeperDistance);
			// real distance between two sleepers
			double dS = 0;
			if (fits > 1)
				dS = (length - sleeperDistance) / (fits - 1);
			double d = (sleeperDistance - sleeperWidth) / 2;
			for (int i = 0; i < fits; i++) {
				g.fillRect((int) d, -sleeperLength / 2, sleeperWidth, sleeperLength);
				d += dS;
			}
		}
		if (layer == 1) {
			if (colorOver != null)
				g.setColor(ColorUtils.mix(railColour, colorOver));
			else
				g.setColor(railColour);
			g.fillRect(0, -railDistance / 2 - railSize / 2, (int) length, railSize);
			g.fillRect(0, railDistance / 2 - railSize / 2, (int) length, railSize);
		}

		g.setTransform(t);

	}

	public static void paintConnections(Graphics2D g,
		ArrayList<DirectedPoint> mainConnections) {
		g.setStroke(new BasicStroke(5));
		g.setColor(connectionColor);
		for (DirectedPoint d : mainConnections) {
			// querlinie
			int dx = (int) ((double) sleeperLength / 2 * d.getAAngle().add(Angle.A_90)
				.getCos());
			int dy = (int) ((double) sleeperLength / 2 * d.getAAngle().add(Angle.A_90)
				.getSin());
			g.drawLine(d.getX() + dx, d.getY() + dy, d.getX() - dx, d.getY() - dy);
			// pfeil richtung
			dx = (int) (15.d * d.getAAngle().getCos());
			dy = (int) (15.d * d.getAAngle().getSin());
			g.drawLine(d.getX(), d.getY(), d.getX() + dx, d.getY() + dy);
		}
	}

	public static void paintCurvedTrack(Graphics2D g, PointF center, Angle startAngle,
		Angle endAngle, int radius, Color colorOver, int layer) {
		AffineTransform t = g.getTransform();
		g.translate(center.x, center.y);
		g.rotate(startAngle.getRadian());
		double deltaAngleRad = endAngle.sub(startAngle).getRadian();

		if (layer == 0) {
			if (colorOver != null)
				g.setColor(ColorUtils.mix(bedColour, colorOver));
			else
				g.setColor(bedColour);
			float perimeter = (float) (Math.PI * 2 * radius * deltaAngleRad / (2 * Math.PI));
			int sleepers = (int) Math.round(perimeter / sleeperDistance);
			double dA = 0; // real angle between sleepers
			if (sleepers > 1)
				dA = (perimeter - sleeperDistance) / (sleepers - 1);
			dA *= deltaAngleRad / perimeter;
			g.rotate(((double) sleeperDistance * 0.5) / perimeter * deltaAngleRad);

			g.translate(0, -radius);
			for (int i = 0; i < sleepers; i++) {
				g.fillRect(-sleeperWidth / 2, -sleeperLength / 2, sleeperWidth,
					sleeperLength);
				g.rotate(dA, 0, radius);
			}
		}
		if (layer == 1) {
			if (colorOver != null)
				g.setColor(ColorUtils.mix(railColour, colorOver));
			else
				g.setColor(railColour);
			g.setStroke(railStroke);
			int r = radius + railDistance / 2;

			double radianToDegreeFactor = 180.d / Math.PI;

			Arc2D.Double arc = new Arc2D.Double(-r, -r, r * 2, r * 2, 90, -deltaAngleRad
				* radianToDegreeFactor, Arc2D.OPEN);
			g.draw(arc);
			r = radius - railDistance / 2;
			arc = new Arc2D.Double(-r, -r, r * 2, r * 2, 90, -deltaAngleRad
				* radianToDegreeFactor, Arc2D.OPEN);
			g.draw(arc);
		}

		g.setTransform(t);
	}

}
