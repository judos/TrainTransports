package model.tracks.builders;

import model.tracks.CurvedTrack;
import model.tracks.Track;
import model.tracks.TrackType;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 08.03.2015
 * @author Julian Schelker
 */
public class CurvedWithConstraintBuilder extends TrackBuilder {

	private CurvedTrack				track;
	private TrackBuildConstraint	constraint;
	private boolean					isLeft;

	public CurvedWithConstraintBuilder(TrackBuildConstraint c, TrackType trackType) {
		this.track = new CurvedTrack(CurvedTrack.STANDARD_CURVE_RADIUS, c.getDirPoint()
			.getPointF(), Angle.A_0, Angle.A_0);
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

		this.track.center = start.getPointF().movePoint(dAngle.add(start.getAAngle()),
			this.track.radius);

		Angle beta = this.track.center.getAAngleTo(mapTarget);
		if (this.isLeft) {
			this.track.startAngle = beta.add(Angle.A_90);
			this.track.endAngle = start.getAAngle().sub(Angle.A_180);
		} else {
			this.track.startAngle = start.getAAngle();
			this.track.endAngle = beta.add(Angle.A_90);
		}
	}

}
