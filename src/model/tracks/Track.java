package model.tracks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import view.DrawableLayered;
import view.TrackDrawer;
import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 29.01.2015
 * @author Julian Schelker
 */
public abstract class Track implements DrawableLayered {

	/**
	 * distance between the rails
	 */
	public static final int							railDistance	= 20;

	/**
	 * the thickness of the rails
	 */
	public static final int							railSize		= 4;

	/**
	 * the length of the sleepers is a bit bigger than the distance between
	 * rails
	 */
	public static final int							sleeperLength	= railDistance + 10;

	/**
	 * width of the sleepers, size along the railway
	 */
	public static final int							sleeperWidth	= 10;

	/**
	 * space between two sleepers
	 */
	public static final int							sleeperDistance	= sleeperWidth + 5;

	public static final Color						bedColour		= Color.ORANGE.darker();
	public static final Color						railColour		= Color.DARK_GRAY;
	public static final Color						connectionColor	= Color.BLUE;
	public static final Stroke						railStroke		= new BasicStroke(railSize);

	protected transient SimpleList<DirectedPoint>	connectionPoints;
	protected transient Color						colorOver;

	protected Set<TrackConnection>[]				connections;

	@SuppressWarnings("unchecked")
	public Track() {
		connections = new Set[2];
		connections[0] = new HashSet<TrackConnection>();
		connections[1] = new HashSet<TrackConnection>();
	}

	protected abstract void initializeConnectionPoints();

	public ArrayList<DirectedPoint> getConnectionPoints() {
		if (this.connectionPoints == null)
			initializeConnectionPoints();
		return this.connectionPoints;
	}

	public void paintConnections(Graphics2D g) {
		TrackDrawer.paintConnections(g, getConnectionPoints());
	}

	public abstract double getTrackLength();

	/**
	 * @param index
	 *            positional index [0,1]
	 * @return the direction is always pointing into forward direction on the
	 *         track, if you are facing backward angle must be rotated by 180°
	 */
	public abstract DirectedPoint getDirPointForIndex(double index);

	@Override
	public void paint(Graphics2D g, int layer) {
		if (layer == 1)
			this.colorOver = null;
	}

	public abstract Track copy();

	public void setColor(Color color) {
		this.colorOver = color;
	}

	public abstract boolean contains(PointI point);

	/**
	 * @param trackPos
	 * @param speed
	 * @param targetTrack
	 * @return remainingSpeed
	 */
	public double positionalMove(TrackPosition trackPos, double speed, Track targetTrack) {
		// handles the special case when speed is negative, and delegates the
		// main purpose
		int direction = trackPos.direction;
		double sign = Math.signum(speed);
		if (speed < 0)
			direction = 1 - direction;
		double remainingSpeed = positionalMoveAbsSpeed(trackPos, direction, Math.abs(speed),
			targetTrack);
		return sign * remainingSpeed;
	}

	/**
	 * @param trackPos
	 * @param direction
	 * @param remainingSpeed
	 *            must be positive
	 * @param targetTrack
	 * @return
	 */
	private double positionalMoveAbsSpeed(TrackPosition trackPos, int direction,
		double remainingSpeed, Track targetTrack) {
		double trackLength = getTrackLength();
		double wayRemainsOnTrack = Math.abs((1 - direction) * trackLength - trackPos.position);
		if (remainingSpeed < wayRemainsOnTrack) {
			if (direction == 0)
				trackPos.position += remainingSpeed;
			else
				trackPos.position -= remainingSpeed;
			return 0;
		}

		// Track, new direction for that connection
		Set<TrackConnection> connections = getConnectionsForEndpoint(1 - direction);
		TrackConnection target = getConnectionForTrack(targetTrack, connections);
		if (target == null && connections.size() > 0)
			target = connections.iterator().next();
		else if (target == null)
			throw new RuntimeException("no track available");

		trackPos.direction = target.directionOnConnectedTrack;
		trackPos.position = target.getPositionOnTargetTrack();
		trackPos.track = target.connected;

		return remainingSpeed - wayRemainsOnTrack;
	}

	public Set<TrackConnection> getConnectionsForEndpoint(int i) {
		if (i != 0 && i != 1)
			throw new InvalidParameterException("only endpoint 0 and 1 exist");
		return this.connections[i];
	}

	public void dispose() {
		for (int i = 0; i <= 1; i++)
			for (TrackConnection x : this.connections[i])
				x.connected.disposeTrack(this);
	}

	private void disposeTrack(Track track) {
		for (int i = 0; i <= 1; i++) {
			TrackConnection c = getConnectionForTrack(track, this.connections[i]);
			if (c != null)
				this.connections[i].remove(c);
		}
	}

	public static TrackConnection getConnectionForTrack(Track t, Set<TrackConnection> c) {
		for (TrackConnection x : c)
			if (x.connected == t)
				return x;
		return null;
	}

	public boolean tryAndConnect(Track t) {
		int i = 0;
		for (DirectedPoint c1 : getConnectionPoints()) {
			int j = 0;
			for (DirectedPoint c2 : t.getConnectionPoints()) {
				if (pointsConnect(c1, c2)) {
					this.connections[i].add(new TrackConnection(t, j));
					t.connections[j].add(new TrackConnection(this, i));
					return true;
				}
				j++;
			}
			i++;
		}
		return false;
	}

	private boolean pointsConnect(DirectedPoint c1, DirectedPoint c2) {
		if (c1.getPoint().distance(c2.getPoint()) <= 2) {
			if (c1.getAAngle().almostEquals(c2.getAAngle().add(Angle.A_180),
				Angle.fromDegree(3)))
				return true;
		}
		return false;
	}
}
