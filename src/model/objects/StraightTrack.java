package model.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.LineI;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.data.serialization.RStorable;
import ch.judos.generic.graphics.ColorUtils;

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
		double length = this.start.distance(this.end);
		AffineTransform t = g.getTransform();
		g.translate(this.start.x, this.start.y);
		g.rotate(this.start.getAngleTo(this.end));

		if (layer == 0) {
			if (this.colorOver != null)
				g.setColor(ColorUtils.mix(bedColour, this.colorOver));
			else
				g.setColor(bedColour);

			int fits = (int) Math.round(length / sleeperDistance);
			// real distance between two sleepers
			double dS = 0;
			if (fits > 1)
				dS = (length - sleeperDistance) / (fits - 1);
			double d = (sleeperDistance - sleeperWidth) / 2;
			for (int i = 0; i < fits; i++) {
				g.fillRect((int) d, -sleeperLength / 2, sleeperWidth, sleeperLength);
				d += dS;
			}
		}
		if (layer == 1) {
			if (this.colorOver != null)
				g.setColor(ColorUtils.mix(railColour, this.colorOver));
			else
				g.setColor(railColour);
			g.fillRect(0, -railDistance / 2 - railSize / 2, (int) length, railSize);
			g.fillRect(0, railDistance / 2 - railSize / 2, (int) length, railSize);
		}

		g.setTransform(t);

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
