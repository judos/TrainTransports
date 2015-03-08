package model.objects.trackBuilders;

import model.objects.StraightTrack;
import model.objects.Track;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 08.03.2015
 * @author Julian Schelker
 */
public class StraightNoConstraintBuilder extends TrackBuilder {
	private StraightTrack	track;

	public StraightNoConstraintBuilder(PointI start) {
		this(start, start);
	}

	public StraightNoConstraintBuilder(PointI start, PointI end) {
		this.track = new StraightTrack(start, end);
	}

	@Override
	public void updateWithTarget(PointI mapTarget) {
		this.track.end = mapTarget;
	}

	@Override
	public Track getTrack() {
		return this.track;
	}
}
