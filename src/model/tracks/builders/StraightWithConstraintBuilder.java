package model.tracks.builders;

import model.tracks.StraightTrack;
import model.tracks.Track;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 08.03.2015
 * @author Julian Schelker
 */
public class StraightWithConstraintBuilder extends TrackBuilder {

	private TrackBuildConstraint	constraint;
	private StraightTrack			track;

	public StraightWithConstraintBuilder(TrackBuildConstraint trackBuildConstraint) {
		this.constraint = trackBuildConstraint;
		PointI start = this.constraint.getDirPoint().getPoint();
		this.track = new StraightTrack(start, start);
	}

	@Override
	public void updateWithTarget(PointI mapTarget) {
		DirectedPoint start = this.constraint.getDirPoint();
		Angle beta = start.getPointF().getAAngleTo(mapTarget);
		double length = start.getPoint().distance(mapTarget);

		double actualLength = length * beta.sub(start.getAAngle()).getCos();
		PointF end = this.track.start.f().movePoint(start.getAAngle(), actualLength);
		this.track.end = end.getPointRounded();
	}

	@Override
	public Track getTrack() {
		return this.track;
	}

}
