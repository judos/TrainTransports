package controller.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import model.Mouse;
import model.TrackBuildConstraint;
import model.input.InputType;
import model.input.KeyEvent2;
import model.input.MouseEvent2;
import model.map.Map;
import model.objects.StraightTrack;
import model.objects.TrackBuilder;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class BuildSimpleTrackTool implements ToolI {

	private Map							map;
	private TrackBuilder				track;
	private State						state;
	private int							currentConnection;
	private List<TrackBuildConstraint>	constraints;

	enum State {
		// the tool is ready and no input is processed yet
		READY,
		// the first connection has already been chosen
		STARTED;
	}

	@Override
	public void initialize(Map map) {
		this.map = map;
		setInitialState();
	}

	private void setInitialState() {
		this.track = new StraightTrack.NoConstraintBuilder(new Point(-10, 0),
				new Point(10, 0));
		this.state = State.READY;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawAbsolute(Graphics2D g) {
		if (this.state == State.READY) {
			drawSampleTrack(g);
		} else if (this.state == State.STARTED) {
		}
	}

	@Override
	public void drawInMap(Graphics2D g) {
		drawConnections(g);
		if (this.state == State.READY) {
		} else if (this.state == State.STARTED) {
			drawCurrentTrackLayout(g);
		}
	}

	private void drawConnections(Graphics2D g) {
		this.map.drawConnections(g);
	}

	private void drawCurrentTrackLayout(Graphics2D g) {
		Point mouse = Mouse.getMouseMapPoint();
		if (mouse != null)
			this.track.updateWithTarget(mouse);
		this.track.paint(g);
	}

	private void drawSampleTrack(Graphics2D g) {
		Point p = Mouse.getMousePoint();
		g.translate(p.x, p.y);
		this.track.paint(g);
		g.translate(-p.x, -p.y);
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		if (m.getType() == InputType.PRESS
				&& m.getButton() == MouseEvent.BUTTON3)
			setInitialState();
		if (m.getType() == InputType.PRESS
				&& m.getButton() == MouseEvent.BUTTON1) {
			if (this.state == State.READY) {
				this.constraints = this.map.getTrackConnectionsFrom(m
						.getMapPosition());
				if (constraints == null || constraints.size() == 0)
					this.track = new StraightTrack.NoConstraintBuilder(
							m.getMapPosition());
				else {
					this.currentConnection = 0;
					this.track = new StraightTrack.WithConstraintBuilder(
							constraints.get(0));
				}
				this.state = State.STARTED;
			} else if (this.state == State.STARTED) {
				this.track.updateWithTarget(m.getMapPosition());
				this.map.addTrack(this.track.getTrackNew());
				setInitialState();
			}
		}
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getType() == InputType.PRESS && e.getKeyCode() == KeyEvent.VK_TAB) {
			if (this.state == State.STARTED) {
				if (this.constraints != null && this.constraints.size() > 1) {
					this.currentConnection = (this.currentConnection + 1)
							% this.constraints.size();
					this.track = new StraightTrack.WithConstraintBuilder(
							constraints.get(this.currentConnection));
					return true;
				}
			}
		}
		return false;
	}

}
