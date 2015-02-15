package controller;

import model.Mouse;
import model.input.KeyHandler;
import model.input.MouseHandler;
import model.input.MouseWheelHandler;
import model.map.Map;
import model.map.Scroll;
import model.objects.CurvedTrack;
import view.DebugInformationView;
import ch.judos.generic.data.geometry.PointF;
import controller.menu.MenuController;
import controller.tools.ToolHandlerController;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class GameController {

	private DrawableManager			drawManager;
	private InputManager			inputManager;
	private Map						map;
	private MenuController			menuController;
	private ToolHandlerController	toolHandler;
	private DebugInformationView	debug;
	private Scroll					scroll;

	public GameController(DrawableManager drawManager, InputManager mm) {
		this.drawManager = drawManager;
		this.inputManager = mm;

		this.scroll = new Scroll(this.drawManager.getViewSize());
		this.scroll.setRelativeTo(this.drawManager.getComponent());
		Mouse.initialize(this.scroll, this.drawManager.getComponent());
		this.inputManager.setScrollObject(this.scroll);
		this.drawManager.setScrollObject(this.scroll);

		this.map = new Map();

		this.toolHandler = new ToolHandlerController(this.map, this.scroll);
		this.menuController = new MenuController(this.toolHandler);

		this.debug = new DebugInformationView();

		addDrawableObjects();
		addInputKeyListeners();
		addInputMouseListeners();
		this.inputManager.addLast((MouseWheelHandler) this.scroll);

		// just for testing
		map.addTrack(new CurvedTrack(160, new PointF(200, 200), 0f, Math.PI - 2));
		// map.addTrack(new CurvedTrack(100, new PointF(200, 200), 1f, Math.PI -
		// 1));
	}

	private void addInputMouseListeners() {
		// first object receives events first
		this.inputManager.addLast((MouseHandler) this.menuController);
		this.inputManager.addLast((MouseHandler) this.toolHandler);
		this.inputManager.addLast((MouseHandler) this.scroll);
	}

	private void addInputKeyListeners() {
		// first object receives events first
		this.inputManager.addLast(this.debug);
		this.inputManager.addLast((KeyHandler) this.toolHandler);
		this.inputManager.addLast((KeyHandler) this.menuController);
		this.inputManager.addLast((KeyHandler) this.scroll);
	}

	private void addDrawableObjects() {
		// last object is drawn over all the previous ones
		this.drawManager.addDrawable(this.map);
		this.drawManager.addDrawable(this.menuController);
		this.drawManager.addDrawable(this.toolHandler);
		this.drawManager.addDrawable(this.debug);

	}

}
