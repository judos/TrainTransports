package model.objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import model.DirectedPoint;
import model.TrackBuildConstraint;
import ch.judos.generic.games.unitCoordination.PointF;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class StraightTrack extends Track {

	protected Point	start;
	protected Point	end;

	public StraightTrack(Point start, Point end) {
		this.start = start;
		this.end = end;
		initializeMainConnections();
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
		float angle =
			(float) Math.atan2(this.end.y - this.start.y, this.end.x - this.start.x);
		this.mainConnections.add(new DirectedPoint(this.end.x, this.end.y, angle));
		this.mainConnections.add(new DirectedPoint(this.start.x, this.start.y,
			(float) (angle - Math.PI)));
	}

	public static class WithConstraintBuilder extends TrackBuilder {

		private TrackBuildConstraint	constraint;
		private StraightTrack			track;

		public WithConstraintBuilder(TrackBuildConstraint trackBuildConstraint) {
			this.constraint = trackBuildConstraint;
			Point start = this.constraint.getDirPoint().getPoint();
			this.track = new StraightTrack(start, start);
		}

		@Override
		public void updateWithTarget(Point mapTarget) {
			DirectedPoint start = this.constraint.getDirPoint();
			double beta =
				Math.atan2(mapTarget.y - start.getY(), mapTarget.x - start.getX());
			double length =
				Math.hypot(mapTarget.y - start.getY(), mapTarget.x - start.getX());

			double actualLength = length * Math.cos(beta - start.getAngle());
			PointF end =
				new PointF(actualLength * Math.cos(start.getAngle()), actualLength
					* Math.sin(start.getAngle()));
			end.addI(this.track.start);
			this.track.end = end.getPoint();
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public Track getTrack() {
			return this.track;
		}
	}

	public static class NoConstraintBuilder extends TrackBuilder {
		private StraightTrack	track;

		public NoConstraintBuilder(Point start) {
			this(start, start);
		}

		public NoConstraintBuilder(Point start, Point end) {
			this.track = new StraightTrack(start, end);
		}

		public void setEnd(Point end) {
			this.track.end = end;
		}

		@Override
		public void updateWithTarget(Point mapTarget) {
			this.track.end = mapTarget;
		}

		@Override
		public boolean isValid() {
			return true;
		}

		@Override
		public Track getTrack() {
			return this.track;
		}
	}

	@Override
	public Track copy() {
		return new StraightTrack(this.start, this.end);
	}

}
