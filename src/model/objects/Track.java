package model.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import model.DirectedPoint;
import view.DrawableLayered;

/**
 * @since 29.01.2015
 * @author Julian Schelker
 */
public abstract class Track implements DrawableLayered {

	public static final int				railDistance	= 20;
	public static final int				railSize		= 4;
	public static final int				sleeperLength	= railDistance + 10;
	public static final int				sleeperWidth	= 10;
	public static final int				sleeperDistance	= sleeperWidth + 5;
	public static final Color			bedColour		= Color.ORANGE.darker();
	public static final Color			railColour		= Color.DARK_GRAY;
	public static final Color			connectionColor	= Color.BLUE;
	public static final Stroke			railStroke		= new BasicStroke(railSize);

	protected ArrayList<DirectedPoint>	mainConnections;

	public Track() {
		this.mainConnections = new ArrayList<DirectedPoint>();
	}

	protected abstract void initializeMainConnections();

	public ArrayList<DirectedPoint> getMainConnections() {
		return this.mainConnections;
	}

	public void paintConnections(Graphics2D g) {
		g.setStroke(new BasicStroke(5));
		g.setColor(connectionColor);
		for (DirectedPoint d : this.mainConnections) {
			// querlinie
			int dx = (int) ((double) sleeperLength / 2 * Math.cos(d.getAngle() + Math.PI
					/ 2));
			int dy = (int) ((double) sleeperLength / 2 * Math.sin(d.getAngle() + Math.PI
					/ 2));
			g.drawLine(d.getX() + dx, d.getY() + dy, d.getX() - dx, d.getY() - dy);
			// pfeil richtung
			dx = (int) (15.d * Math.cos(d.getAngle()));
			dy = (int) (15.d * Math.sin(d.getAngle()));
			g.drawLine(d.getX(), d.getY(), d.getX() + dx, d.getY() + dy);
		}
	}

	public abstract Track copy();
}
