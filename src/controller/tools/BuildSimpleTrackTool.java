package controller.tools;

import java.awt.Graphics2D;
import java.awt.Point;

import model.Mouse;
import model.input.KeyEvent2;
import model.input.MouseEvent2;
import model.map.Map;
import model.objects.StraightTrack;
import model.objects.StraightTrack.Builder;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class BuildSimpleTrackTool implements ToolI {

	private Map		map;
	private Builder	track;

	enum State {
		// the tool is ready and no input is processed yet
		READY,
		// the first connection has already been chosen
		STARTED;
	}

	@Override
	public void initialize(Map map) {
		this.map = map;
		this.track = new StraightTrack.Builder(new Point(-10, 0));
		this.track.setEnd(new Point(10, 0));
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics2D g) {
		Point p = Mouse.getMousePoint();
		g.translate(p.x, p.y);
		this.track.getTrack().paint(g, 0);
		this.track.getTrack().paint(g, 1);

		g.translate(-p.x, -p.y);
	}

	@Override
	public boolean handles(MouseEvent2 m) {

		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		return false;
	}

}
