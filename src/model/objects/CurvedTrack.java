package model.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.data.serialization.RStorable;
import ch.judos.generic.graphics.ColorUtils;

/**
 * @since 29.01.2015
 * @author Julian Schelker
 */
public class CurvedTrack extends Track implements RStorable {

	public static final int	STANDARD_CURVE_RADIUS	= 200;

	public PointF			center;

	// always clock-wise oriented, angle is stored in RADIAN
	public Angle			startAngle;
	// always clock-wise oriented, angle is stored in RADIAN
	public Angle			endAngle;

	public int				radius;

	/**
	 * used for RStorage to create objects
	 */
	@SuppressWarnings("unused")
	private CurvedTrack() {
		this.mainConnections = null;
	}

	public CurvedTrack(int radius, PointF center, Angle aStart, Angle aEnd) {
		this.center = center;
		this.radius = radius;
		this.startAngle = aStart;
		this.endAngle = aEnd;
		initializeMainConnections();
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		AffineTransform t = g.getTransform();
		g.translate(this.center.x, this.center.y);
		g.rotate(this.startAngle.getRadian());
		double deltaAngleRad = this.endAngle.sub(this.startAngle).getRadian();

		if (layer == 0) {
			if (this.colorOver != null)
				g.setColor(ColorUtils.mix(bedColour, this.colorOver));
			else
				g.setColor(bedColour);
			float perimeter = (float) (Math.PI * 2 * this.radius * deltaAngleRad / (2 * Math.PI));
			int sleepers = (int) Math.round(perimeter / sleeperDistance);
			double dA = 0; // real angle between sleepers
			if (sleepers > 1)
				dA = (perimeter - sleeperDistance) / (sleepers - 1);
			dA *= deltaAngleRad / perimeter;
			g.rotate(((double) sleeperDistance * 0.5) / perimeter * deltaAngleRad);

			g.translate(0, -this.radius);
			for (int i = 0; i < sleepers; i++) {
				g.fillRect(-sleeperWidth / 2, -sleeperLength / 2, sleeperWidth,
					sleeperLength);
				g.rotate(dA, 0, this.radius);
			}
		}
		if (layer == 1) {
			if (this.colorOver != null)
				g.setColor(ColorUtils.mix(railColour, this.colorOver));
			else
				g.setColor(railColour);
			g.setStroke(railStroke);
			int r = this.radius + railDistance / 2;

			double radianToDegreeFactor = 180.d / Math.PI;

			Arc2D.Double arc = new Arc2D.Double(-r, -r, r * 2, r * 2, 90, -deltaAngleRad
				* radianToDegreeFactor, Arc2D.OPEN);
			g.draw(arc);
			r = this.radius - railDistance / 2;
			arc = new Arc2D.Double(-r, -r, r * 2, r * 2, 90, -deltaAngleRad
				* radianToDegreeFactor, Arc2D.OPEN);
			g.draw(arc);
		}

		g.setTransform(t);
		super.paint(g, layer);
	}

	@Override
	public void initializeMainConnections() {
		this.mainConnections = new SimpleList<DirectedPoint>();
		PointF p1 = this.center.movePoint(this.startAngle.sub(Angle.A_90), this.radius);
		this.mainConnections.add(new DirectedPoint(p1.getPoint(), this.startAngle
			.add(Angle.A_180)));

		PointF p2 = this.center.movePoint(this.endAngle.sub(Angle.A_90), this.radius);
		this.mainConnections.add(new DirectedPoint(p2.getPoint(), this.endAngle));
	}

	@Override
	public Track copy() {
		return new CurvedTrack(this.radius, this.center, this.startAngle, this.endAngle);
	}

	@Override
	public boolean contains(PointI mouse) {
		float r = this.center.distanceTo(mouse) - this.radius;
		if (r < -Track.sleeperLength / 2 || r > Track.sleeperLength / 2)
			return false;
		Angle angle = this.center.getAAngleTo(mouse);
		angle.turnClockwise(Angle.A_90); // subtracted because start and end
											// angle of the curved track are
											// defined strangely

		return angle.inIntervalUncapped(this.startAngle, this.endAngle);
	}

}
