package controller.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

import model.Mouse;
import model.TrackBuildConstraint;
import model.input.InputType;
import model.input.KeyEvent2;
import model.input.MouseEvent2;
import model.map.Map;
import model.map.Scroll;
import model.objects.StraightTrack;
import model.objects.StraightTrack.NoConstraintBuilder;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class BuildSimpleTrackTool implements ToolI {

	private Map					map;
	private NoConstraintBuilder	track;
	private State				state;
	private Scroll				scroll;

	enum State {
		// the tool is ready and no input is processed yet
		READY,
		// the first connection has already been chosen
		STARTED;
	}

	@Override
	public void initialize(Map map, Scroll scroll) {
		this.map = map;
		this.scroll = scroll;
		setInitialState();
	}

	private void setInitialState() {
		this.track = new StraightTrack.NoConstraintBuilder(new Point(-10, 0));
		this.track.setEnd(new Point(10, 0));
		this.state = State.READY;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics2D g) {
		if (this.state == State.READY) {
			drawConnections(g);
			drawSampleTrack(g);
		} else if (this.state == State.STARTED) {
			drawCurrentTrackLayout(g);
		}
	}

	private void drawConnections(Graphics2D g) {
		// TODO:
	}

	private void drawCurrentTrackLayout(Graphics2D g) {
		this.track.updateWithTarget(Mouse.getMouseMapPoint());
		AffineTransform original = g.getTransform();
		AffineTransform t = g.getTransform();
		this.scroll.applyTransformTo(t);
		g.setTransform(t);
		this.track.paint(g);
		g.setTransform(original);
	}

	private void drawSampleTrack(Graphics2D g) {
		Point p = Mouse.getMousePoint();
		g.translate(p.x, p.y);
		this.track.paint(g);
		g.translate(-p.x, -p.y);
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		if (m.getType() == InputType.PRESS && m.getButton() == MouseEvent.BUTTON3)
			setInitialState();
		if (m.getType() == InputType.PRESS && m.getButton() == MouseEvent.BUTTON1) {
			if (this.state == State.READY) {
				TrackBuildConstraint[] connections =
					this.map.getTrackConnectionsFrom(m.getMapPosition());
				if (connections == null)
					this.track =
						new StraightTrack.NoConstraintBuilder(m.getMapPosition());

				this.state = State.STARTED;
			} else if (this.state == State.STARTED) {
				this.track.updateWithTarget(m.getMapPosition());
				this.map.addTrack(this.track.getTrack());
				setInitialState();
			}
		}
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		return false;
	}

}
