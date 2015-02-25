package model.objects;

import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 25.02.2015
 * @author Julian Schelker
 */
public abstract class CurvedTrackBuilder extends TrackBuilder {

	protected CurvedTrack	track;

	@Override
	public void updateWithTarget(PointI mapTarget) {
		// no implementation needed
	}

	@Override
	public Track getTrack() {
		return this.track;
	}

	public PointF getTrackCenter() {
		return this.track.center;
	}

	public abstract void setEndAngle(Angle gamma);

	public abstract PointI getEndPoint();
}
