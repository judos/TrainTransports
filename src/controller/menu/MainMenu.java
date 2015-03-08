package controller.menu;

import controller.tools.ToolHandlerI;

/**
 * @since 07.02.2015
 * @author Julian Schelker
 */
public class MainMenu extends AbstractMenu {

	public MainMenu(MenuNavigationI navigation, ToolHandlerI toolHandler) {
		super(navigation, toolHandler);
		this.addEntry("Tracks", () -> navigation.pushMenu(new TrackMenu(navigation,
			toolHandler)));

		this.addEntry("Exit", () -> navigation.quit());
	}
}
