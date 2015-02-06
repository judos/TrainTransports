package model.objects;

import java.awt.BasicStroke;
import java.awt.Color;
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

	protected static final Stroke		railStroke		= new BasicStroke(railSize);

	public Track() {
		this.mainConnections = new ArrayList<DirectedPoint>();
		initializeMainConnections();
	}

	protected abstract void initializeMainConnections();

	public ArrayList<DirectedPoint> getMainConnections() {
		return this.mainConnections;
	}
}
