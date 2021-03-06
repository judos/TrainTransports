package model.tracks;

import java.awt.Graphics2D;

import view.TrackDrawer;
import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.geometry.*;
import ch.judos.generic.data.serialization.RStorable;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class StraightTrack extends Track implements RStorable {

	public PointI	start;
	public PointI	end;

	/**
	 * used for RStorage to create objects
	 */
	@SuppressWarnings("unused")
	private StraightTrack() {
		this.connectionPoints = null;
	}

	public StraightTrack(PointI start, PointI end) {
		this.start = start;
		this.end = end;
		initializeConnectionPoints();
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		TrackDrawer.drawStraightFromTo(g, this.start, this.end, this.colorOver, layer);
		super.paint(g, layer);
	}

	@Override
	protected void initializeConnectionPoints() {
		this.connectionPoints = new SimpleList<DirectedPoint>();
		Angle angle = this.start.getAAngleTo(this.end);
		this.connectionPoints.add(new DirectedPoint(this.end, angle));
		this.connectionPoints.add(new DirectedPoint(this.start, angle.sub(Angle.A_180)));
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

	@Override
	public double getTrackLength() {
		// XXX: improve performance by storing this
		return this.start.distance(this.end);
	}

	@Override
	public DirectedPoint getDirPointForIndex(double index) {
		Angle angle = this.start.getAAngleTo(this.end);
		PointF point = this.start.f().movePoint(angle, index * getTrackLength());
		return new DirectedPoint(point.i(), angle);
	}

}
