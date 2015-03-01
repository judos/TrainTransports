import java.util.Optional;

import javax.swing.SwingUtilities;

import view.Gui;
import controller.DrawableManager;
import controller.GameController;
import controller.InputManager;

/**
 * @since 27.01.2015
 * @author Julian Schelker
 */
public class LauncherSimTrackJ {

	public static void main(String[] args) {
		// System.setProperty("sun.java2d.trace", "log");
		System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.accthreshold", "0");

		LauncherSimTrackJ l = new LauncherSimTrackJ();
		SwingUtilities.invokeLater(l::init);

	}

	private GameController	game;
	private Gui				gui;

	private void init() {
		this.gui = new Gui(Optional.of(this::onQuit));

		DrawableManager dm = new DrawableManager(this.gui);
		InputManager mm = new InputManager(this.gui);

		this.game = new GameController(dm, mm, this::onQuit);
		this.gui.startView(60);
	}

	private Object	onQuitLock	= new Object();
	private void onQuit() {
		synchronized (onQuitLock) {
			try {
				this.game.quit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.gui.quit();
			System.exit(0);
		}
	}

}
