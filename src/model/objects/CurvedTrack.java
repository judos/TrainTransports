package model.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import model.DirectedPoint;
import model.TrackBuildConstraint;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.graphics.ColorUtils;

/**
 * @since 29.01.2015
 * @author Julian Schelker
 */
public class CurvedTrack extends Track {

	public static final int	STANDARD_CURVE_RADIUS	= 100;

	private PointF			center;

	// always clock-wise oriented, angle is stored in RADIAN
	private double			startAngle;
	// always clock-wise oriented, angle is stored in RADIAN
	private double			endAngle;

	private int				radius;

	public static CurvedTrack createWithDegreeAngles(int radius, PointF center,
			double angleStart, double angleEnd) {
		double degreeToRadianFactor = Math.PI / 180;
		return new CurvedTrack(radius, center, angleStart * degreeToRadianFactor,
				angleEnd * degreeToRadianFactor);
	}

	public CurvedTrack(int radius, PointF center, double angleStart, double angleEnd) {
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
			if (this.colorOver != null)
				g.setColor(ColorUtils.mix(bedColour, this.colorOver));
			else
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
			if (this.colorOver != null)
				g.setColor(ColorUtils.mix(railColour, this.colorOver));
			else
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

		super.paint(g, layer);
	}

	@Override
	protected void initializeMainConnections() {
		int x = (int) (this.center.x + (double) this.radius
				* Math.cos(this.startAngle - Math.PI / 2));
		int y = (int) (this.center.y + (double) this.radius
				* Math.sin(this.startAngle - Math.PI / 2));
		this.mainConnections.add(new DirectedPoint(x, y, this.startAngle + Math.PI));

		x = (int) (this.center.x + (double) this.radius
				* Math.cos(this.endAngle - Math.PI / 2));
		y = (int) (this.center.y + (double) this.radius
				* Math.sin(this.endAngle - Math.PI / 2));
		this.mainConnections.add(new DirectedPoint(x, y, this.endAngle));
	}

	@Override
	public Track copy() {
		return new CurvedTrack(this.radius, this.center, this.startAngle, this.endAngle);
	}

	public static class NoConstraintBuilder extends TrackBuilder {

		private CurvedTrack	track;
		private Point		start;

		public NoConstraintBuilder(int radius, Point center, double angleStart,
				double angleEnd) {
			this.track = new CurvedTrack(radius, new PointF(center), angleStart, angleEnd);
		}

		public NoConstraintBuilder(Point startingPoint) {
			this.track = new CurvedTrack(STANDARD_CURVE_RADIUS,
					new PointF(startingPoint), 0, 0);
			if (startingPoint == null)
				System.out.println("Constructing with null value");
			this.start = startingPoint;
		}
		@Override
		public Track getTrack() {
			return this.track;
		}

		@Override
		public void updateWithTarget(PointI mapTarget) {
			if (this.start == null)
				return;
			double phi = Math.atan2(start.y - mapTarget.y, mapTarget.x - start.x);
			double alpha = Math.hypot(mapTarget.y - start.y, mapTarget.x - start.x)
					/ (2 * STANDARD_CURVE_RADIUS) * Math.PI;
			if (alpha > Math.PI)
				alpha = Math.PI;
			double beta = (Math.PI - alpha) / 2;

			this.track.center = new PointF(this.start.x + (float) this.track.radius
					* Math.cos(beta + phi), this.start.y - this.track.radius
					* Math.sin(beta + phi));
			this.track.endAngle = -Math.PI / 2 - phi - beta;
			this.track.startAngle = this.track.endAngle - alpha;

		}

		@Override
		public boolean isValid() {
			// TODO Auto-generated method stub
			return true;
		}

	}

	public static class WithConstraintBuilder extends TrackBuilder {

		private CurvedTrack				track;
		private TrackBuildConstraint	constraint;

		public WithConstraintBuilder(TrackBuildConstraint c) {
			this.track = new CurvedTrack(STANDARD_CURVE_RADIUS, c.getDirPoint()
					.getPointF(), 0, 0);
			this.constraint = c;
		}

		@Override
		public Track getTrack() {
			return this.track;
		}

		@Override
		public void paint(Graphics2D g) {
			super.paint(g);
			g.setColor(Color.red);
			g.fillRect(this.track.center.getXI(), this.track.center.getYI(), 5, 5);
			g.setColor(Color.green);
			DirectedPoint start = this.constraint.getDirPoint();
			if (start != null)
				g.fillRect(start.getX(), start.getY(), 5, 5);
		}

		@Override
		public void updateWithTarget(PointI mapTarget) {
			DirectedPoint start = this.constraint.getDirPoint();
			this.track.center = new PointF(start.getX() + (double) this.track.radius
					* Math.cos(start.getAngle() - Math.PI / 2), start.getY()
					+ (double) this.track.radius
					* Math.sin(start.getAngle() - Math.PI / 2));
			double alpha = Math.atan2(mapTarget.y - this.track.center.y, mapTarget.x
					- this.track.center.x);
			this.track.startAngle = alpha + Math.PI / 2;
			this.track.endAngle = start.getAngle() + Math.PI;
		}
		@Override
		public boolean isValid() {
			// TODO Auto-generated method stub
			return true;
		}

	}

	@Override
	public boolean contains(PointI mouse) {
		float r = this.center.distanceTo(mouse) - this.radius;
		if (r < -Track.sleeperLength / 2 || r > Track.sleeperLength / 2)
			return false;
		Angle angle = this.center.getAAngleTo(mouse);
		angle.turnClockwise(Math.PI / 2); // subtracted because start and end
											// angle of the curved track are
											// defined strangely
		if (this.startAngle < this.endAngle)
			return this.startAngle <= angle.getRadian()
					&& angle.getRadian() <= this.endAngle;
		else
			return angle.getRadian() >= this.startAngle
					|| angle.getRadian() <= this.endAngle;
	}

}
