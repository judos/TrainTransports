package model.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import model.TrackBuildConstraint;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.LineI;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.graphics.ColorUtils;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class StraightTrack extends Track {

	protected PointI	start;
	protected PointI	end;

	public StraightTrack(PointI start, PointI end) {
		this.start = start;
		this.end = end;
		initializeMainConnections();
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		double length = this.start.distance(this.end);
		AffineTransform t = g.getTransform();
		g.translate(this.start.x, this.start.y);
		g.rotate(this.start.getAngleTo(this.end));

		if (layer == 0) {
			if (this.colorOver != null)
				g.setColor(ColorUtils.mix(bedColour, this.colorOver));
			else
				g.setColor(bedColour);
			// TODO: draw sleepers distributed better
			for (int x = 0; x < length; x += sleeperDistance) {
				g.fillRect(x, -sleeperLength / 2, sleeperWidth, sleeperLength);
			}
		}
		if (layer == 1) {
			if (this.colorOver != null)
				g.setColor(ColorUtils.mix(railColour, this.colorOver));
			else
				g.setColor(railColour);
			g.fillRect(0, -railDistance / 2 - railSize / 2, (int) length, railSize);
			g.fillRect(0, railDistance / 2 - railSize / 2, (int) length, railSize);
		}

		g.setTransform(t);

		super.paint(g, layer);
	}

	@Override
	protected void initializeMainConnections() {
		float angle = (float) Math.atan2(this.end.y - this.start.y, this.end.x
				- this.start.x);
		this.mainConnections.add(new DirectedPoint(this.end.x, this.end.y, angle));
		this.mainConnections.add(new DirectedPoint(this.start.x, this.start.y,
				(float) (angle - Math.PI)));
	}

	public static class WithConstraintBuilder extends TrackBuilder {

		private TrackBuildConstraint	constraint;
		private StraightTrack			track;

		public WithConstraintBuilder(TrackBuildConstraint trackBuildConstraint) {
			this.constraint = trackBuildConstraint;
			PointI start = this.constraint.getDirPoint().getPoint();
			this.track = new StraightTrack(start, start);
		}

		@Override
		public void updateWithTarget(PointI mapTarget) {
			DirectedPoint start = this.constraint.getDirPoint();
			double beta = Math.atan2(mapTarget.y - start.getY(),
					mapTarget.x - start.getX());
			double length = Math.hypot(mapTarget.y - start.getY(),
					mapTarget.x - start.getX());

			double actualLength = length * Math.cos(beta - start.getAngle());
			PointF end = new PointF(actualLength * Math.cos(start.getAngle()),
					actualLength * Math.sin(start.getAngle()));
			end.addI(this.track.start);
			this.track.end = end.getPointRounded();
		}

		@Override
		public boolean isValid() {
			// TODO: implement
			return true;
		}

		@Override
		public Track getTrack() {
			return this.track;
		}
	}

	public static class NoConstraintBuilder extends TrackBuilder {
		private StraightTrack	track;

		public NoConstraintBuilder(PointI start) {
			this(start, start);
		}

		public NoConstraintBuilder(PointI start, PointI end) {
			this.track = new StraightTrack(start, end);
		}

		@Override
		public void updateWithTarget(PointI mapTarget) {
			this.track.end = mapTarget;
		}

		@Override
		public boolean isValid() {
			// TODO: implement
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

	@Override
	public boolean contains(PointI point) {
		LineI l = new LineI(this.start, this.end);
		double dist = l.ptLineDist(point);
		if (dist > Track.sleeperLength / 2)
			return false;
		dist = l.ptLineDistAlongOutside(point);
		if (Math.abs(dist) > 0)
			return false;
		return true;
	}

}
