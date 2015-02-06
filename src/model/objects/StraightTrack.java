package model.objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class StraightTrack extends Track {

	protected Point	start;
	public Point	end;

	public StraightTrack(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		int length =
			(int) Math.hypot(this.start.x - this.end.x, this.start.y - this.end.y);
		AffineTransform t = g.getTransform();
		g.translate(this.start.x, this.start.y);
		g.rotate(Math.atan2(this.end.y - this.start.y, this.end.x - this.start.x));

		if (layer == 0) {
			g.setColor(bedColour);
			for (int x = 0; x < length; x += sleeperDistance) {
				g.fillRect(x, -sleeperLength / 2, sleeperWidth, sleeperLength);
			}
		}
		if (layer == 1) {
			// g.setColor(Color.LIGHT_GRAY);
			// g.drawLine(this.start.x, this.start.y, this.end.x, this.end.y);
			g.setColor(railColour);
			g.fillRect(0, -railDistance / 2 - railSize / 2, length, railSize);
			g.fillRect(0, railDistance / 2 - railSize / 2, length, railSize);
		}

		g.setTransform(t);
	}

	@Override
	protected void initializeMainConnections() {
		// TODO:
	}

}
