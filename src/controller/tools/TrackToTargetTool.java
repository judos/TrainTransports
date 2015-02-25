package controller.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import model.Mouse;
import model.TrackBuildConstraint;
import model.input.InputType;
import model.input.KeyEvent2;
import model.input.MouseEvent2;
import model.map.Map;
import model.objects.CurvedTrack;
import model.objects.CurvedTrack.LeftBuilder;
import model.objects.CurvedTrack.RightBuilder;
import model.objects.CurvedTrackBuilder;
import model.objects.StraightTrack;
import model.objects.TrackBuilder;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.LineI;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 15.02.2015
 * @author Julian Schelker
 */
public class TrackToTargetTool extends AbstractTool {

	private Map							map;
	private List<TrackBuildConstraint>	startConstraints;
	private ArrayList<TrackBuilder>		tracks;
	private int							currentStartC;
	private PointI						startPoint;

	@Override
	public boolean handles(MouseEvent2 m) {
		if (m.getButton() == MouseEvent.BUTTON1 && m.getType() == InputType.PRESS) {
			if (this.state == State.READY) {
				this.startConstraints = this.map.getTrackConnectionsFrom(m
						.getMapPosition());
				this.startPoint = m.getMapPosition();
				this.state = State.STARTED;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getType() == InputType.PRESS) {
			if (e.getKeyCode() == KeyEvent.VK_TAB) {
				if (this.state == State.STARTED) {
					if (this.startConstraints.size() > 1) {
						this.currentStartC = (this.currentStartC + 1)
								% this.startConstraints.size();
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void initialize(Map map) {
		this.map = map;
		this.tracks = new ArrayList<TrackBuilder>();
		this.state = State.READY;
	}

	@Override
	public void drawAbsolute(Graphics2D g) {
		PointI p = Mouse.getMousePoint();
		if (p != null) {
			g.setColor(Color.black);
			g.drawString("Complex", p.x, p.y);
		}
	}

	@Override
	public void drawInMap(Graphics2D g) {
		this.map.drawConnections(g);
		this.tracks.clear();
		updateCurrentTrackLayout();

		for (TrackBuilder b : this.tracks)
			b.paint(g);
	}

	private void updateCurrentTrackLayout() {
		if (this.state == State.STARTED) {
			PointI target = Mouse.getMouseMapPoint();
			if (target == null)
				return;

			// Get constraints
			TrackBuildConstraint sc = null;
			if (this.startConstraints.size() > 0)
				sc = this.startConstraints.get(this.currentStartC);

			List<TrackBuildConstraint> targetC = this.map.getTrackConnectionsFrom(target);
			TrackBuildConstraint tc = null;
			if (targetC.size() > 0)
				tc = targetC.get(0);

			if (tc == null) {
				if (sc == null)
					this.tracks.add(new StraightTrack.NoConstraintBuilder(
							this.startPoint, target));
				else {
					LineI i = new LineI(sc.getDirPoint(), 100);
					double dy = i.ptLineDistSigned(target);
					CurvedTrackBuilder t;
					if (dy > 0)
						t = new LeftBuilder(sc);
					else
						t = new RightBuilder(sc);

					// angle between straight track and line to curved track
					// center
					Angle beta = Angle.fromTriangleOH(CurvedTrack.STANDARD_CURVE_RADIUS,
							t.getTrackCenter().distance(target));
					if (beta == null)
						return;
					Angle alpha = t.getTrackCenter().getAAngleTo(target);
					Angle gamma;
					if (dy > 0)
						gamma = alpha.sub(beta);
					else
						gamma = alpha.add(beta);

					t.setEndAngle(gamma);
					this.tracks.add(t);

					this.tracks.add(new StraightTrack.NoConstraintBuilder(
							t.getEndPoint(), target));

				}
			}
		}
	}
	@Override
	public void setInitialState() {
		super.setInitialState();

	}

}
