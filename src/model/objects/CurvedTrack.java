package model.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import model.TrackBuildConstraint;
import model.TrackType;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.graphics.ColorUtils;

/**
 * @since 29.01.2015
 * @author Julian Schelker
 */
public class CurvedTrack extends Track {

	public static final int	STANDARD_CURVE_RADIUS	= 200;

	PointF					center;

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

	public CurvedTrack(int radius, PointF center, Angle aStart, Angle aEnd) {
		this.center = center;
		this.radius = radius;
		this.startAngle = aStart.getRadian();
		this.endAngle = aEnd.getRadian();
		initializeMainConnections();
	}

	@Deprecated
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
		double deltaAngleRad = (this.endAngle + 2 * Math.PI - this.startAngle)
				% (2 * Math.PI);

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
	protected void initializeMainConnections() {
		this.mainConnections.clear();

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
		private PointI		start;
		private boolean		isLeft;

		public NoConstraintBuilder(int radius, PointI center, double angleStart,
				double angleEnd) {
			this.track = new CurvedTrack(radius, new PointF(center), angleStart, angleEnd);
		}

		public NoConstraintBuilder(PointI startingPoint, TrackType trackType) {
			this.track = new CurvedTrack(STANDARD_CURVE_RADIUS,
					new PointF(startingPoint), 0, 0);
			if (startingPoint == null)
				System.out.println("Constructing with null value");
			this.start = startingPoint;
			this.isLeft = trackType == TrackType.LEFT;
		}
		@Override
		public Track getTrack() {
			return this.track;
		}

		@Override
		public void updateWithTarget(PointI mapTarget) {
			if (this.start == null)
				return;
			Angle alpha = this.start.getAAngleTo(mapTarget);
			Angle beta = Angle.fromRadianUncapped(this.start.distance(mapTarget)
					/ (2 * STANDARD_CURVE_RADIUS) * Math.PI);
			beta.setIfHigherTo(Math.PI);
			Angle phi = (Angle.A_180.sub(beta)).div(2);

			Angle fromStartToCenter;
			if (this.isLeft)
				fromStartToCenter = alpha.sub(phi);
			else
				fromStartToCenter = alpha.add(phi);
			this.track.center = this.start.f().movePoint(fromStartToCenter,
					STANDARD_CURVE_RADIUS);

			Angle addEnd;
			if (this.isLeft)
				addEnd = Angle.A_270;
			else
				addEnd = Angle.A_90;
			this.track.endAngle = alpha.sub(phi).add(addEnd).getRadian();
			this.track.startAngle = this.track.endAngle - beta.getRadian();

		}
	}

	public static class WithConstraintBuilder extends TrackBuilder {

		private CurvedTrack				track;
		private TrackBuildConstraint	constraint;
		private boolean					isLeft;

		public WithConstraintBuilder(TrackBuildConstraint c, TrackType trackType) {
			this.track = new CurvedTrack(STANDARD_CURVE_RADIUS, c.getDirPoint()
					.getPointF(), 0, 0);
			this.constraint = c;
			this.isLeft = (trackType == TrackType.LEFT);
		}

		@Override
		public Track getTrack() {
			return this.track;
		}

		@Override
		public void updateWithTarget(PointI mapTarget) {
			DirectedPoint start = this.constraint.getDirPoint();

			Angle dAngle = (this.isLeft ? Angle.A_270 : Angle.A_90);

			this.track.center = start.getPointF().movePoint(
					dAngle.add(start.getAAngle()), this.track.radius);

			Angle beta = this.track.center.getAAngleTo(mapTarget);
			if (this.isLeft) {
				this.track.startAngle = beta.add(Angle.A_90).getRadian();
				this.track.endAngle = start.getAAngle().sub(Angle.A_180).getRadian();
			} else {
				this.track.startAngle = start.getAAngle().getRadian();
				this.track.endAngle = beta.add(Angle.A_90).getRadian();
			}
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

	public static class RightBuilder extends CurvedTrackBuilder {

		public RightBuilder(TrackBuildConstraint sc) {
			DirectedPoint dp = sc.getDirPoint();
			// turn right from the connection point 90°, then move by radius of
			// the curve
			PointF centerP = dp.getPointF().movePoint(dp.getAAngle().add(Angle.A_90),
					STANDARD_CURVE_RADIUS);
			// the end point starts with the angle of the connection point
			this.track = new CurvedTrack(STANDARD_CURVE_RADIUS, centerP,
					absAngleToTrackRight(dp.getAAngle()), Angle.A_0);
		}

		@Override
		public PointI getEndPoint() {
			return this.track.mainConnections.get(1).getPoint();
		}

		@Override
		public void paint(Graphics2D g) {
			// TODO: debug code
			g.setColor(Color.red);
			g.fillRect(this.track.center.getXI(), this.track.center.getYI(), 5, 5);
			super.paint(g);
		}

		@Override
		public void setEndAngle(Angle angle) {
			this.track.endAngle = absAngleToTrackRight(angle).getRadian();
			this.track.initializeMainConnections();
		}

	}

	public static class LeftBuilder extends CurvedTrackBuilder {

		public LeftBuilder(TrackBuildConstraint sc) {
			DirectedPoint dp = sc.getDirPoint();
			// turn left from the connection point 90°, then move by radius of
			// the curve
			PointF centerP = dp.getPointF().movePoint(dp.getAAngle().sub(Angle.A_90),
					STANDARD_CURVE_RADIUS);
			// the end point starts with the angle of the connection point
			this.track = new CurvedTrack(STANDARD_CURVE_RADIUS, centerP, Angle.A_0,
					absAngleToTrackLeft(dp.getAAngle()));
		}

		@Override
		public PointI getEndPoint() {
			return this.track.mainConnections.get(0).getPoint();
		}

		@Override
		public void setEndAngle(Angle angle) {
			this.track.startAngle = absAngleToTrackLeft(angle).getRadian();
			this.track.initializeMainConnections();
		}

		@Override
		public void paint(Graphics2D g) {
			// TODO: debug code
			g.setColor(Color.red);
			g.fillRect(this.track.center.getXI(), this.track.center.getYI(), 5, 5);
			super.paint(g);
		}

	}

	/**
	 * converts an absolute angle that shows the track direction into a curved
	 * track angle seen relative from the center of the curved track<br>
	 * <b>Note:</b> This is only valid for curved tracks that bend to the left
	 * 
	 * @param aAngle
	 * @return
	 */
	public static Angle absAngleToTrackLeft(Angle angle) {
		return angle.add(Angle.A_180);
	}

	/**
	 * converts an absolute angle that shows the track direction into a curved
	 * track angle seen relative from the center of the curved track<br>
	 * <b>Note:</b> This is only valid for curved tracks that bend to the right
	 * 
	 * @param aAngle
	 * @return
	 */
	public static Angle absAngleToTrackRight(Angle angle) {
		return angle;
	}

}
