package controller.menu;

import controller.tools.ToolHandlerI;

/**
 * @since 07.02.2015
 * @author Julian Schelker
 */
public class MainMenu extends AbstractMenu {

	public MainMenu(MenuNavigationI navigation, ToolHandlerI toolHandler) {
		super(navigation, toolHandler);
		this.addEntry("Build Tracks", () -> this.navigation.pushMenu(new BuildMenu(
			this.navigation, this.toolHandler)));
		this.addEntry("Remove Tracks", () -> {});
		this.addEntry("Exit", () -> System.exit(0));
	}
}
