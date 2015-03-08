package model.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import view.DrawableLayered;
import view.TrackDrawer;
import ch.judos.generic.data.concurrent.SimpleList;
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

	public static final Color						bedColour		= Color.ORANGE
																		.darker();
	public static final Color						railColour		= Color.DARK_GRAY;
	public static final Color						connectionColor	= Color.BLUE;
	public static final Stroke						railStroke		= new BasicStroke(
																		railSize);

	protected transient SimpleList<DirectedPoint>	mainConnections;
	protected transient Color						colorOver;

	public Track() {
	}

	protected abstract void initializeMainConnections();

	public ArrayList<DirectedPoint> getMainConnections() {
		if (this.mainConnections == null)
			initializeMainConnections();
		return this.mainConnections;
	}

	public void paintConnections(Graphics2D g) {
		TrackDrawer.paintConnections(g, getMainConnections());
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
}
