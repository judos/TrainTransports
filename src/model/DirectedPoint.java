package model;

import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.data.geometry.PointI;

/**
 * a point in 2d[int] space that hints into a direction
 * 
 * @since 30.01.2015
 * @author Julian Schelker
 */
public class DirectedPoint {
	protected PointI	pos;
	protected double	angle;

	/**
	 * @param x
	 * @param y
	 * @param angle
	 *            in RADIAN <br>
	 *            0°= [+1|0], oriented clockwise, 90°= [0|+1], ...
	 */
	public DirectedPoint(int x, int y, double angle) {
		this.pos = new PointI(x, y);
		this.angle = angle;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return this.pos.x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return this.pos.y;
	}

	/**
	 * @return the angle in RADIAN
	 */
	public double getAngle() {
		return angle;
	}

	public PointI getPoint() {
		return this.pos;
	}

	public PointF getPointF() {
		return new PointF(this.pos);
	}

}
