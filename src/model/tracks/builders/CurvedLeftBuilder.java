package model.tracks.builders;

import model.tracks.CurvedTrack;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 08.03.2015
 * @author Julian Schelker
 */
public class CurvedLeftBuilder extends CurvedTrackBuilder {
	public CurvedLeftBuilder(TrackBuildConstraint sc) {
		DirectedPoint dp = sc.getDirPoint();
		// turn left from the connection point 90°, then move by radius of
		// the curve
		PointF centerP = dp.getPointF().movePoint(dp.getAAngle().sub(Angle.A_90),
			CurvedTrack.STANDARD_CURVE_RADIUS);
		// the end point starts with the angle of the connection point
		this.track = new CurvedTrack(CurvedTrack.STANDARD_CURVE_RADIUS, centerP,
			Angle.A_0, absAngleToTrackLeft(dp.getAAngle()));
	}

	@Override
	public PointI getEndPoint() {
		return this.track.getConnectionPoints().get(0).getPoint();
	}

	@Override
	public void setEndAngle(Angle angle) {
		this.track.startAngle = absAngleToTrackLeft(angle);
		this.track.initializeConnectionPoints();
	}

}
