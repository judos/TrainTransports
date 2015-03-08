package model.objects.trackBuilders;

import model.objects.CurvedTrack;
import model.objects.Track;
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
