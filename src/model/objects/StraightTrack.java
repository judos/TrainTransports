package model.objects;

import java.awt.Graphics2D;

import view.TrackDrawer;
import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.LineI;
import ch.judos.generic.data.geometry.PointI;
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
		this.mainConnections = null;
	}

	public StraightTrack(PointI start, PointI end) {
		this.start = start;
		this.end = end;
		initializeMainConnections();
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		TrackDrawer.drawStraightFromTo(g, this.start, this.end, this.colorOver, layer);
		super.paint(g, layer);
	}

	@Override
	protected void initializeMainConnections() {
		this.mainConnections = new SimpleList<DirectedPoint>();
		Angle angle = this.start.getAAngleTo(this.end);
		this.mainConnections.add(new DirectedPoint(this.end, angle));
		this.mainConnections.add(new DirectedPoint(this.start, angle.sub(Angle.A_180)));
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

}
