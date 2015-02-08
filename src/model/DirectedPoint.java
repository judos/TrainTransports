package model;

import java.awt.Point;

/**
 * a point in 2d[int] space that hints into a direction
 * 
 * @since 30.01.2015
 * @author Julian Schelker
 */
public class DirectedPoint {
	protected int		x;
	protected int		y;
	protected double	angle;

	/**
	 * @param x
	 * @param y
	 * @param angle in RADIAN <br>
	 *            0°= [+1|0], oriented clockwise, 90°= [0|+1], ...
	 */
	public DirectedPoint(int x, int y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the angle in RADIAN
	 */
	public double getAngle() {
		return angle;
	}

	public double distance(Point otherPoint) {
		return Math.hypot(this.x - otherPoint.x, this.y - otherPoint.y);
	}

	public Point getPoint() {
		return new Point(this.x, this.y);
	}

}
