package controller.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.List;

import model.Mouse;
import model.input.InputType;
import model.input.KeyEvent2;
import model.input.MouseEvent2;
import model.map.Map;
import model.objects.Track;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 09.02.2015
 * @author Julian Schelker
 */
public class RemoveTrackTool extends AbstractTool {

	private Map						map;
	public static final BasicStroke	stroke	= new BasicStroke(5);

	@Override
	public void dispose() {

	}

	@Override
	public void initialize(Map map) {
		this.map = map;
	}

	@Override
	public void drawAbsolute(Graphics2D g) {
		PointI mouse = Mouse.getMousePoint();
		if (mouse != null) {
			g.setColor(Color.red);
			g.setStroke(stroke);
			g.drawLine(mouse.x - 10, mouse.y - 10, mouse.x + 10, mouse.y + 10);
			g.drawLine(mouse.x - 10, mouse.y + 10, mouse.x + 10, mouse.y - 10);
		}
	}

	@Override
	public void drawInMap(Graphics2D g) {
		PointI mouse = Mouse.getMouseMapPoint();
		List<Track> tracks = this.map.getTrackByPoint(mouse);
		for (Track t : tracks) {
			t.setColor(Color.red);
		}
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		if (m.getButton() == MouseEvent.BUTTON1 && m.getType() == InputType.PRESS) {
			PointI mouse = Mouse.getMouseMapPoint();
			List<Track> tracks = this.map.getTrackByPoint(mouse);
			for (Track t : tracks) {
				this.map.removeTrack(t);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		return false;
	}

	@Override
	public boolean isInInitialState() {
		return true;
	}

	@Override
	public void setInitialState() {
	}

}
