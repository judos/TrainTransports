package model.tracks.builders;

import model.tracks.CurvedTrack;
import model.tracks.Track;
import model.tracks.TrackType;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 08.03.2015
 * @author Julian Schelker
 */
public class CurvedNoConstraintBuilder extends TrackBuilder {

	private CurvedTrack	track;
	private PointI		start;
	private boolean		isLeft;

	public CurvedNoConstraintBuilder(int radius, PointI center, Angle angleStart,
		Angle angleEnd) {
		this.track = new CurvedTrack(radius, new PointF(center), angleStart, angleEnd);
	}

	public CurvedNoConstraintBuilder(PointI startingPoint, TrackType trackType) {
		this.track = new CurvedTrack(CurvedTrack.STANDARD_CURVE_RADIUS, new PointF(
			startingPoint), Angle.A_0, Angle.A_0);
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
			/ (2 * CurvedTrack.STANDARD_CURVE_RADIUS) * Math.PI);
		beta.setIfHigherTo(Math.PI);
		Angle phi = (Angle.A_180.sub(beta)).div(2);

		Angle fromStartToCenter;
		if (this.isLeft)
			fromStartToCenter = alpha.sub(phi);
		else
			fromStartToCenter = alpha.add(phi);
		this.track.center = this.start.f().movePoint(fromStartToCenter,
			CurvedTrack.STANDARD_CURVE_RADIUS);

		Angle addEnd;
		if (this.isLeft)
			addEnd = Angle.A_270;
		else
			addEnd = Angle.A_90;
		this.track.endAngle = alpha.sub(phi).add(addEnd);
		this.track.startAngle = this.track.endAngle.sub(beta);

	}

}
