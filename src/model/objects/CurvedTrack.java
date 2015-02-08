package model.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import model.DirectedPoint;

/**
 * @since 29.01.2015
 * @author Julian Schelker
 */
public class CurvedTrack extends Track {

	private Point center;

	// always clock-wise oriented, angle is stored in RADIAN
	private double startAngle;
	// always clock-wise oriented, angle is stored in RADIAN
	private double endAngle;

	private int radius;

	public static CurvedTrack createWithDegreeAngles(int radius, Point center,
			double angleStart, double angleEnd) {
		double degreeToRadianFactor = Math.PI / 180;
		return new CurvedTrack(radius, center, angleStart
				* degreeToRadianFactor, angleEnd * degreeToRadianFactor);
	}

	public CurvedTrack(int radius, Point center, double angleStart,
			double angleEnd) {
		this.center = center;
		this.radius = radius;
		this.startAngle = angleStart;
		this.endAngle = angleEnd;
		initializeMainConnections();
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		AffineTransform t = g.getTransform();
		g.translate(this.center.x, this.center.y);
		g.rotate(this.startAngle);
		double deltaAngle = (this.endAngle + 2 * Math.PI - this.startAngle)
				% (2 * Math.PI);

		if (layer == 0) {
			g.setColor(bedColour);
			float perimeter = (float) (Math.PI * 2 * this.radius * deltaAngle / (2 * Math.PI));
			int sleepers = (int) (perimeter / sleeperDistance);
			g.translate(0, -this.radius);
			for (int i = 0; i < sleepers; i++) {
				g.fillRect(-sleeperWidth / 2, -sleeperLength / 2, sleeperWidth,
						sleeperLength);
				g.rotate(deltaAngle / sleepers, 0, this.radius);
			}
		}
		if (layer == 1) {
			g.setColor(railColour);
			g.setStroke(railStroke);
			int r = this.radius + railDistance / 2;
			double radianToDegreeFactor = 180.d / Math.PI;
			g.drawArc(-r, -r, r * 2, r * 2, 90,
					-(int) (deltaAngle * radianToDegreeFactor));
			r = this.radius - railDistance / 2;
			g.drawArc(-r, -r, r * 2, r * 2, 90,
					-(int) (deltaAngle * radianToDegreeFactor));
		}

		g.setTransform(t);
		g.setColor(Color.red);
		g.fillRect(0, 0, 5, 5);
	}

	@Override
	protected void initializeMainConnections() {
		int x = (int) (this.center.x + (double) this.radius
				* Math.cos(this.startAngle + Math.PI / 2));
		int y = (int) (this.center.y + (double) this.radius
				* Math.sin(this.startAngle + Math.PI / 2));
		this.mainConnections.add(new DirectedPoint(x, y, this.startAngle
				+ Math.PI));

		x = (int) (this.center.x + (double) this.radius
				* Math.cos(this.endAngle + Math.PI / 2));
		y = (int) (this.center.y + (double) this.radius
				* Math.sin(this.endAngle + Math.PI / 2));
		this.mainConnections.add(new DirectedPoint(x, y, this.endAngle));
	}

	@Override
	public Track copy() {
		return new CurvedTrack(this.radius, this.center, this.startAngle,
				this.endAngle);
	}
}
