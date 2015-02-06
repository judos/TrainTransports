package model.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 * @since 29.01.2015
 * @author Julian Schelker
 */
public class CurvedTrack extends Track {

	private Point	center;

	// always clock-wise
	private float	startAngle;
	private float	endAngle;

	private int		radius;

	public CurvedTrack(int radius) {
		this.center = new Point(200, 200);
		this.radius = radius;
		this.startAngle = 0;
		this.endAngle = 360;
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		final float degreeToRadianFactor = (float) (Math.PI / 180);
		AffineTransform t = g.getTransform();
		g.translate(this.center.x, this.center.y);
		g.rotate(this.startAngle * degreeToRadianFactor);
		// +359 +1 prevents that modulo caps off 360° curves
		float deltaAngle = (this.endAngle + 359 - this.startAngle) % 360 + 1;

		if (layer == 0) {
			g.setColor(bedColour);
			float perimeter = (float) (Math.PI * 2 * this.radius * deltaAngle / 360);
			int sleepers = (int) (perimeter / sleeperDistance);
			g.translate(0, -this.radius);
			for (int i = 0; i < sleepers; i++) {
				g.fillRect(-sleeperWidth / 2, -sleeperLength / 2, sleeperWidth,
					sleeperLength);
				g.rotate(deltaAngle * degreeToRadianFactor / sleepers, 0, this.radius);
			}
		}
		if (layer == 1) {
			g.setColor(railColour);
			g.setStroke(railStroke);
			int r = this.radius + railDistance / 2;
			g.drawArc(-r, -r, r * 2, r * 2, 90, -(int) deltaAngle);
			r = this.radius - railDistance / 2;
			g.drawArc(-r, -r, r * 2, r * 2, 90, -(int) deltaAngle);
		}

		g.setTransform(t);
		g.setColor(Color.red);
		g.fillRect(0, 0, 5, 5);
	}

	@Override
	protected void initializeMainConnections() {
		// TODO Auto-generated method stub

	}

}
