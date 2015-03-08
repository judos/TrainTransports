package controller.tools;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
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
import model.objects.Track;
import model.objects.TrackBuilder;
import ch.judos.generic.data.geometry.Angle;
import ch.judos.generic.data.geometry.LineI;
import ch.judos.generic.data.geometry.PointF;
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
	private ArrayList<Track>			demoTrack;
	private List<TrackBuildConstraint>	targetC;
	private int							targetCIndex;

	@Override
	public void initialize(Map map) {
		this.map = map;
		this.tracks = new ArrayList<TrackBuilder>();
		this.state = State.READY;
		this.demoTrack = new ArrayList<Track>();
		this.demoTrack.add(new StraightTrack(new PointI(10, -30), new PointI(-10, 30)));
		this.demoTrack.add(new StraightTrack(new PointI(-10, -30), new PointI(10, 30)));
	}

	@Override
	public void dispose() {

	}

	@Override
	public boolean handles(MouseEvent2 m) {
		if (m.getButton() == MouseEvent.BUTTON1 && m.getType() == InputType.PRESS) {
			if (this.state == State.READY) {
				this.startConstraints = this.map.getTrackConnectionsFrom(m
					.getMapPosition());
				this.startPoint = m.getMapPosition();
				this.state = State.STARTED;
				return true;
			} else if (this.state == State.STARTED) {
				for (TrackBuilder b : this.tracks)
					this.map.addTrack(b.getTrackNew());
				setInitialState();
			}
		}
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getType() == InputType.PRESS) {
			if (e.getKeyCode() == KeyEvent.VK_TAB) {
				if (this.state == State.STARTED) {
					if (this.startConstraints.size() > 1
						&& (this.targetC == null || this.targetC.size() == 0))
						this.currentStartC = (this.currentStartC + 1)
							% this.startConstraints.size();
					else if (this.targetC != null && this.targetC.size() > 0)
						this.targetCIndex = (this.targetCIndex + 1) % this.targetC.size();

					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void drawAbsolute(Graphics2D g) {
		PointI p = Mouse.getMousePoint();
		if (p == null)
			return;

		if (this.state == State.READY) {
			AffineTransform t2 = g.getTransform();
			g.translate(p.x + 30, p.y + 10);
			g.scale(0.5, 0.5);
			for (Track t : this.demoTrack)
				t.paint(g, 0);
			for (Track t : this.demoTrack)
				t.paint(g, 1);

			g.setTransform(t2);
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
		this.targetC = null;
		if (this.state == State.STARTED) {
			PointI target = Mouse.getMouseMapPoint();
			if (target == null)
				return;

			// Get constraints
			TrackBuildConstraint sc = null;
			if (this.startConstraints.size() > 0)
				sc = this.startConstraints.get(this.currentStartC);

			this.targetC = this.map.getTrackConnectionsFrom(target);
			TrackBuildConstraint tc = null;
			if (this.targetC.size() > 0) {
				if (this.targetCIndex >= this.targetC.size())
					this.targetCIndex = 0;
				tc = this.targetC.get(this.targetCIndex);
			}

			// switch start and target
			if (tc != null && sc == null) {
				sc = tc;
				tc = null;
				target = this.startPoint;
			}

			if (tc == null) {
				if (sc == null)
					this.tracks.add(new StraightTrack.NoConstraintBuilder(
						this.startPoint, target));
				else
					trackToTargetWithStartConstraint(sc, target);
			} else if (tc != null && sc != null)
				trackFromTo(sc, tc);
		}
	}

	private void trackFromTo(TrackBuildConstraint sc, TrackBuildConstraint tc) {
		TrackBuildConstraint[] x = {sc, tc};
		double[] dist = new double[2];
		CurvedTrackBuilder[] xb = new CurvedTrackBuilder[2];
		for (int i = 0; i < 2; i++) {
			TrackBuildConstraint cc = x[i]; // current constraint
			TrackBuildConstraint oc = x[1 - i]; // other constraint

			LineI l = new LineI(cc.getDirPoint(), 100);
			dist[i] = l.ptLineDistSigned(oc.getDirPoint().getPoint());

			// depending on curve, minimal distance from line varies
			Angle c = oc.getDirPoint().getAAngle().sub(cc.getDirPoint().getAAngle());
			if ((dist[i] > 0 && c.inInterval(Angle.A_0, Angle.A_180))
				|| (dist[i] < 0 && c.inInterval(Angle.A_180, Angle.A_360)))
				dist[i] -= CurvedTrack.STANDARD_CURVE_RADIUS * (1 + c.getCos());

			if (dist[i] > 0)
				xb[i] = new LeftBuilder(cc);
			else
				xb[i] = new RightBuilder(cc);
		}
		boolean sameCurved = (dist[0] > 0 == dist[1] > 0);
		for (int i = 0; i < 2; i++) {
			PointF c1 = xb[i].getTrackCenter();
			PointF c2 = xb[1 - i].getTrackCenter();
			Angle abs;
			if (sameCurved) {
				Angle beta = Angle.fromTriangleOH(2 * CurvedTrack.STANDARD_CURVE_RADIUS,
					c1.distance(c2));
				if (beta == null)
					return;
				Angle alpha = c1.getAAngleTo(c2);
				if (dist[i] > 0)
					abs = alpha.sub(beta);
				else
					abs = alpha.add(beta);
			} else {
				abs = c1.getAAngleTo(c2);
			}

			xb[i].setEndAngle(abs);

			this.tracks.add(xb[i]);
		}

		this.tracks.add(new StraightTrack.NoConstraintBuilder(xb[0].getEndPoint(), xb[1]
			.getEndPoint()));
	}

	private void trackToTargetWithStartConstraint(TrackBuildConstraint sc, PointI target) {
		LineI i = new LineI(sc.getDirPoint(), 100);
		double dy = i.ptLineDistSigned(target);
		CurvedTrackBuilder t;
		if (dy > 0)
			t = new LeftBuilder(sc);
		else
			t = new RightBuilder(sc);

		// angle between straight track and line to curved track
		// center
		Angle beta = Angle.fromTriangleOH(CurvedTrack.STANDARD_CURVE_RADIUS, t
			.getTrackCenter().distance(target));
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

		this.tracks.add(new StraightTrack.NoConstraintBuilder(t.getEndPoint(), target));
	}

	@Override
	public void setInitialState() {
		super.setInitialState();
	}

}
