package controller.tools;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import model.Mouse;
import model.input.InputType;
import model.input.KeyEvent2;
import model.input.MouseEvent2;
import model.map.Map;
import model.tracks.TrackType;
import model.tracks.builders.*;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class BuildSimpleTrackTool extends AbstractTool {

	private Map							map;
	private TrackBuilder				track;
	private int							currentConnection;
	private List<TrackBuildConstraint>	constraints;
	private TrackType					trackType;
	private PointI						startingPoint;

	@Override
	public void initialize(Map map) {
		this.map = map;
		this.trackType = TrackType.STRAIGHT;
		setInitialState();
	}

	private void updatePreviewAndBuilder() {
		if (this.state == State.READY) {
			if (this.trackType == TrackType.STRAIGHT)
				this.track = new StraightNoConstraintBuilder(new PointI(10, -10),
					new PointI(10, 10));
			if (this.trackType == TrackType.LEFT)
				this.track = new CurvedNoConstraintBuilder(40, new PointI(-20, 0), Angle
					.fromDegree(55.6), Angle.fromDegree(107.2));
			if (this.trackType == TrackType.RIGHT)
				this.track = new CurvedNoConstraintBuilder(40, new PointI(60, 0), Angle
					.fromDegree(252.8), Angle.fromDegree(304.4));
		} else {
			selectTrackBuilder();
		}
	}

	@Override
	public void setInitialState() {
		super.setInitialState();
		updatePreviewAndBuilder();
	}

	@Override
	public void dispose() {
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
		this.map.drawConnections(g);
		if (this.state == State.READY) {
		} else if (this.state == State.STARTED) {
			drawCurrentTrackLayout(g);
		}
	}

	private void drawCurrentTrackLayout(Graphics2D g) {
		PointI mouse = Mouse.getMouseMapPoint();
		if (mouse != null)
			this.track.updateWithTarget(mouse);
		this.track.paint(g);
	}

	private void drawSampleTrack(Graphics2D g) {
		PointI p = Mouse.getMousePoint();
		if (p == null || this.track == null)
			return;
		g.translate(p.x, p.y);
		this.track.paint(g);
		g.translate(-p.x, -p.y);
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		if (m.getType() == InputType.PRESS && m.getButton() == MouseEvent.BUTTON2) {
			this.trackType = this.trackType.prev();
			updatePreviewAndBuilder();
			return true;
		} else if (m.getType() == InputType.PRESS && m.getButton() == MouseEvent.BUTTON1) {
			if (this.state == State.READY) {
				this.constraints = this.map.getTrackConnectionsFrom(m.getMapPosition());
				this.currentConnection = 0;
				this.startingPoint = m.getMapPosition();
				selectTrackBuilder();
				this.state = State.STARTED;
			} else if (this.state == State.STARTED) {
				this.track.updateWithTarget(m.getMapPosition());
				this.map.addTrack(this.track.getTrackNew());
				setInitialState();
			}
		}
		return false;
	}

	private void selectTrackBuilder() {
		if (trackType == TrackType.STRAIGHT) {
			if (constraints == null || constraints.size() == 0)
				this.track = new StraightNoConstraintBuilder(this.startingPoint);
			else {
				this.track = new StraightWithConstraintBuilder(constraints
					.get(this.currentConnection));
			}
		} else {
			if (constraints == null || constraints.size() == 0)
				this.track = new CurvedNoConstraintBuilder(this.startingPoint,
					this.trackType);
			else {
				this.track = new CurvedWithConstraintBuilder(constraints
					.get(this.currentConnection), this.trackType);
			}
		}
	}
	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getType() == InputType.PRESS) {
			if (e.getKeyCode() == KeyEvent.VK_Q) {
				this.trackType = this.trackType.prev();
				updatePreviewAndBuilder();
				return true;
			} else if (e.getKeyCode() == KeyEvent.VK_E) {
				this.trackType = this.trackType.next();
				updatePreviewAndBuilder();
				return true;
			}
			if (e.getKeyCode() == KeyEvent.VK_TAB) {
				if (this.state == State.STARTED) {
					if (this.constraints != null && this.constraints.size() > 1) {
						this.currentConnection = (this.currentConnection + 1)
							% this.constraints.size();
						selectTrackBuilder();
						return true;
					}
				}
			}
		}
		return false;
	}

}
