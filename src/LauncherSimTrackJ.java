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

		SwingUtilities.invokeLater(LauncherSimTrackJ::init);

	}

	private static void init() {
		Gui w = new Gui();

		DrawableManager dm = new DrawableManager(w);
		InputManager mm = new InputManager(w);

		GameController g = new GameController(dm, mm);
		w.startView(60);
	}

}
