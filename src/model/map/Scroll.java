package model.map;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import model.Config;
import model.input.InputType;
import model.input.KeyEvent2;
import model.input.KeyHandler;
import model.input.MouseEvent2;
import model.input.MouseHandler;
import model.input.MouseWheelEvent2;
import model.input.MouseWheelHandler;
import ch.judos.generic.games.BorderlessScrolling;

/**
 * @since 27.01.2015
 * @author Julian Schelker
 */
public class Scroll extends BorderlessScrolling implements KeyHandler, MouseWheelHandler,
	MouseHandler {

	private boolean				scrollLeft;
	private boolean				scrollRight;
	private boolean				scrollUp;
	private boolean				scrollDown;
	private double				zoom;
	private double				targetZoom;
	private static final double	ORIGINAL_ZOOM_VALUE	= 0.9;

	public Scroll(Dimension viewPort) {
		super(viewPort);
		this.zoom = ORIGINAL_ZOOM_VALUE;
		this.targetZoom = ORIGINAL_ZOOM_VALUE;
	}

	@Override
	protected int getFps() {
		return Config.fps;
	}

	@Override
	protected int getScrollBorder() {
		return Config.scrollBorder;
	}

	@Override
	protected int getScrollSpeed() {
		return (int) (Config.scrollSpeed / this.zoom);
	}

	public void updateAndTransform(Graphics2D g) {
		updateZoomFactor();

		AffineTransform t = g.getTransform();
		applyTransformTo(t);
		g.setTransform(t);

		// scroll with keys
		if (this.scrollLeft)
			scrollLeft();
		if (this.scrollRight)
			scrollRight();
		if (this.scrollUp)
			scrollUp();
		if (this.scrollDown)
			scrollDown();
		// scroll by mouse
		update();
	}

	protected void updateZoomFactor() {
		double diff = (this.targetZoom - this.zoom);
		double diffDir = Math.signum(diff);
		diff = Math.abs(diff);
		double diffChange = diff * 0.05;
		if (diffChange > 0 && diffChange < 0.001 * this.targetZoom) {
			diffChange = Math.min(0.001 * this.targetZoom, diff);
		}
		this.zoom += diffDir * diffChange;
	}

	/**
	 * adds the transformation that maps map coordiantes to screen coordiantes
	 * 
	 * @param t
	 * @return
	 */
	public AffineTransform applyTransformTo(AffineTransform t) {
		t.translate(this.viewPort.width / 2, this.viewPort.height / 2);
		t.scale(this.zoom, this.zoom);
		t.translate(-this.viewPort.width / 2, -this.viewPort.height / 2);
		t.translate(-this.locX, -this.locY);
		return t;
	}

	/**
	 * @return transformation that maps screen coordinates to map coordinates
	 */
	public AffineTransform getTransformScreenToMap() {
		// must be exact revers of applyTransformTo
		AffineTransform t = new AffineTransform();
		t.translate(this.locX, this.locY);
		t.translate(this.viewPort.width / 2, this.viewPort.height / 2);
		t.scale(1. / this.zoom, 1. / this.zoom);
		t.translate(-this.viewPort.width / 2, -this.viewPort.height / 2);
		return t;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getKeyCode() == KeyEvent.VK_A)
			this.scrollLeft = e.getType().isPressed();
		if (e.getKeyCode() == KeyEvent.VK_D)
			this.scrollRight = e.getType().isPressed();
		if (e.getKeyCode() == KeyEvent.VK_W)
			this.scrollUp = e.getType().isPressed();
		if (e.getKeyCode() == KeyEvent.VK_S)
			this.scrollDown = e.getType().isPressed();
		return false;
	}

	@Override
	public boolean handles(MouseWheelEvent2 e) {
		if (e.getRotation() < 0) {
			this.targetZoom *= 1.3;
		} else if (e.getRotation() > 0) {
			this.targetZoom /= 1.3;
		}
		return true;
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		if (m.getButton() == MouseEvent.BUTTON2 && m.getType() == InputType.PRESS) {
			this.targetZoom = ORIGINAL_ZOOM_VALUE;
			return true;
		}
		return false;
	}
}
