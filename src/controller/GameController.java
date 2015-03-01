package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.Mouse;
import model.input.KeyHandler;
import model.input.MouseHandler;
import model.input.MouseWheelHandler;
import model.map.Map;
import model.map.Scroll;
import util.RStorableSimpleList;
import view.DebugInformationView;
import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.serialization.DeserializeException;
import ch.judos.generic.data.serialization.ReadableStorageImpl;
import ch.judos.generic.files.FileUtils;
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

	public GameController(DrawableManager drawManager, InputManager mm, Runnable onQuit) {
		this.drawManager = drawManager;
		this.inputManager = mm;

		this.scroll = new Scroll(this.drawManager.getViewSize());
		this.scroll.setRelativeTo(this.drawManager.getComponent());
		Mouse.initialize(this.scroll, this.drawManager.getComponent());
		this.inputManager.setScrollObject(this.scroll);
		this.drawManager.setScrollObject(this.scroll);

		loadMap();

		this.toolHandler = new ToolHandlerController(this.map, this.scroll);
		this.menuController = new MenuController(this.toolHandler, onQuit);

		this.debug = new DebugInformationView();

		addDrawableObjects();
		addInputKeyListeners();
		addInputMouseListeners();
		this.inputManager.addLast((MouseWheelHandler) this.scroll);

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

	private void loadMap() {
		File mapsave = new File("mapsave.txt");
		if (!mapsave.exists()) {
			this.map = new Map();
			return;
		}
		try (BufferedReader reader = FileUtils.getReaderForFile(mapsave)) {
			ReadableStorageImpl rs = getRStorage();
			this.map = (Map) rs.readObject(reader);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DeserializeException e) {
			e.printStackTrace();
		}
	}

	public void quit() throws IOException {
		BufferedWriter wr = FileUtils.getWriterForFile(new File("mapsave.txt"));
		ReadableStorageImpl rs = getRStorage();
		rs.storeTo(this.map, wr);
		wr.close();
	}

	private ReadableStorageImpl getRStorage() {
		ReadableStorageImpl rs = new ReadableStorageImpl();
		rs.addStorableWrapper(SimpleList.class, new RStorableSimpleList.Factory(rs));
		return rs;
	}

}
