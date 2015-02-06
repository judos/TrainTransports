package controller;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;

import model.input.InputType;
import model.input.MouseEvent2;
import model.input.MouseHandler;
import model.map.Map;
import model.map.Scroll;
import model.objects.StraightTrack;
import ch.judos.generic.games.unitCoordination.PointF;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class BuildTrackHandler implements MouseHandler, Drawable2d {

	private Map				map;
	private StraightTrack	track;
	private Scroll			scroll;

	public BuildTrackHandler(Map map, Scroll s) {
		this.scroll = s;
		this.map = map;
		this.track = null;
	}

	@Override
	public boolean handles(MouseEvent2 m) {

		if (m.getType() == InputType.PRESS) {
			if (this.track == null) {
				if (m.getButton() == MouseEvent.BUTTON1) {
					PointF start =
						new PointF(m.getScreenPosition()).add(this.scroll.getPosition());
					Point mouse = MouseInfo.getPointerInfo().getLocation();
					PointF end = new PointF(mouse).add(this.scroll.getPosition());
					this.track = new StraightTrack(start.getPoint(), end.getPoint());
					this.map.addTrack(this.track);
				}
			} else {
				if (m.getButton() != MouseEvent.BUTTON1)
					this.map.removeTrack(this.track);
				this.track = null;
			}
		}
		return true;
	}

	@Override
	public void paint(Graphics2D g) {
		if (this.track != null) {
			Point mouse = MouseInfo.getPointerInfo().getLocation();
			PointF end = new PointF(mouse).add(this.scroll.getPosition());
			this.track.end = end.getPoint();
		}
	}

}
