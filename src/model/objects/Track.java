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

	protected ArrayList<DirectedPoint>	mainConnections;
	protected static final int			railDistance	= 20;
	protected static final int			railSize		= 4;
	protected static final int			sleeperLength	= railDistance + 10;
	protected static final int			sleeperWidth	= 10;
	protected static final int			sleeperDistance	= sleeperWidth + 5;
	protected static final Color		bedColour		= Color.ORANGE.darker();
	protected static final Color		railColour		= Color.DARK_GRAY;
	protected static final Color		connectionColor	= Color.BLUE;

	protected static final Stroke		railStroke		= new BasicStroke(railSize);

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
			int dx = (int) (sleeperLength / 2 * Math.cos(d.getAngle() + Math.PI / 2));
			int dy = (int) (sleeperLength / 2 * Math.sin(d.getAngle() + Math.PI / 2));
			g.drawLine(d.getX() + dx, d.getY() + dy, d.getX() - dx, d.getY() - dy);
			// pfeil richtung
			dx = (int) (15 * Math.cos(d.getAngle()));
			dy = (int) (15 * Math.sin(d.getAngle()));
			g.drawLine(d.getX(), d.getY(), d.getX() + dx, d.getY() + dy);
		}
	}
}
