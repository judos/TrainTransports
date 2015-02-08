package model;

/**
 * a point in 2d[int] space that hints into a direction
 * 
 * @since 30.01.2015
 * @author Julian Schelker
 */
public class DirectedPoint {
	protected int	x;
	protected int	y;
	protected float	angle;

	/**
	 * @param x
	 * @param y
	 * @param angle in RADIAN <br>
	 *            0°= [+1|0], oriented clockwise, 90°= [0|+1], ...
	 */
	public DirectedPoint(int x, int y, float angle) {
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
	public float getAngle() {
		return angle;
	}

}
