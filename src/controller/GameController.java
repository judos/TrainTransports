package controller;

import model.map.Map;
import model.map.Scroll;
import model.objects.CurvedTrack;
import view.DebugInformationView;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class GameController {

	private DrawableManager	draw;
	private InputManager	inputManager;
	private Map				map;

	public GameController(DrawableManager m, InputManager mm) {
		this.draw = m;
		this.inputManager = mm;

		Scroll s = new Scroll(m.getViewSize(), mm);
		s.setRelativeTo(m.getFrame());
		mm.setScrollObject(s);
		this.draw.setScrollObject(s);

		this.map = new Map(s);
		this.draw.addDrawable(map);
		BuildTrackHandler build = new BuildTrackHandler(map, s);
		mm.addFirst(build);
		this.draw.addDrawable(build);

		DebugInformationView debug = new DebugInformationView(mm);
		this.draw.addDrawable(debug);

		// just for testing
		map.addTrack(new CurvedTrack(160));
		map.addTrack(new CurvedTrack(100));
	}

}
