package model.objects.trackBuilders;

import model.TrackBuildConstraint;
import model.objects.CurvedTrack;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 08.03.2015
 * @author Julian Schelker
 */
public class CurvedRightBuilder extends CurvedTrackBuilder {
	public CurvedRightBuilder(TrackBuildConstraint sc) {
		DirectedPoint dp = sc.getDirPoint();
		// turn right from the connection point 90°, then move by radius of
		// the curve
		PointF centerP = dp.getPointF().movePoint(dp.getAAngle().add(Angle.A_90),
			CurvedTrack.STANDARD_CURVE_RADIUS);
		// the end point starts with the angle of the connection point
		this.track = new CurvedTrack(CurvedTrack.STANDARD_CURVE_RADIUS, centerP,
			absAngleToTrackRight(dp.getAAngle()), Angle.A_0);
	}

	@Override
	public PointI getEndPoint() {
		return this.track.getMainConnections().get(1).getPoint();
	}

	@Override
	public void setEndAngle(Angle angle) {
		this.track.endAngle = absAngleToTrackRight(angle);
		this.track.initializeMainConnections();
	}

}
