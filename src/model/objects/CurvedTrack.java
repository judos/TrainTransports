package model.objects;

import java.awt.Graphics2D;

import view.TrackDrawer;
import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.data.serialization.RStorable;

/**
 * @since 29.01.2015
 * @author Julian Schelker
 */
public class CurvedTrack extends Track implements RStorable {

	public static final int	STANDARD_CURVE_RADIUS	= 200;

	public PointF			center;

	// always clock-wise oriented, angle is stored in RADIAN
	public Angle			startAngle;
	// always clock-wise oriented, angle is stored in RADIAN
	public Angle			endAngle;

	public int				radius;

	/**
	 * used for RStorage to create objects
	 */
	@SuppressWarnings("unused")
	private CurvedTrack() {
		this.mainConnections = null;
	}

	public CurvedTrack(int radius, PointF center, Angle aStart, Angle aEnd) {
		this.center = center;
		this.radius = radius;
		this.startAngle = aStart;
		this.endAngle = aEnd;
		initializeMainConnections();
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		TrackDrawer.paintCurvedTrack(g, this.center, this.startAngle, this.endAngle,
			this.radius, this.colorOver, layer);
		super.paint(g, layer);
	}

	@Override
	public void initializeMainConnections() {
		this.mainConnections = new SimpleList<DirectedPoint>();
		PointF p1 = this.center.movePoint(this.startAngle.sub(Angle.A_90), this.radius);
		this.mainConnections.add(new DirectedPoint(p1.getPoint(), this.startAngle
			.add(Angle.A_180)));

		PointF p2 = this.center.movePoint(this.endAngle.sub(Angle.A_90), this.radius);
		this.mainConnections.add(new DirectedPoint(p2.getPoint(), this.endAngle));
	}

	@Override
	public Track copy() {
		return new CurvedTrack(this.radius, this.center, this.startAngle, this.endAngle);
	}

	@Override
	public boolean contains(PointI mouse) {
		float r = this.center.distanceTo(mouse) - this.radius;
		if (r < -Track.sleeperLength / 2 || r > Track.sleeperLength / 2)
			return false;
		Angle angle = this.center.getAAngleTo(mouse);
		angle.turnClockwise(Angle.A_90); // subtracted because start and end
											// angle of the curved track are
											// defined strangely
		return angle.inIntervalUncapped(this.startAngle, this.endAngle);
	}

}
