package controller.menu;

import controller.tools.BuildSimpleTrackTool;
import controller.tools.ToolHandlerI;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class BuildMenu extends AbstractMenu {

	public BuildMenu(MenuNavigationI navigation, ToolHandlerI toolHandler) {
		super(navigation, toolHandler);

		addEntry("SimpleTrack", () -> toolHandler.setTool(new BuildSimpleTrackTool()));
		addEntry("TrackToTarget", () -> {});
		addEntry("TrackWithDir", () -> {});
		addEntry("Back", () -> navigation.popMenu(this));
	}
}
