package controller.menu;

import controller.tools.BuildSimpleTrackTool;
import controller.tools.RemoveTrackTool;
import controller.tools.ToolHandlerI;
import controller.tools.TrackToTargetTool;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class TrackMenu extends AbstractMenu {

	public TrackMenu(MenuNavigationI navigation, ToolHandlerI toolHandler) {
		super(navigation, toolHandler);

		addEntry("Simple", () -> toolHandler.setTool(new BuildSimpleTrackTool()));
		addEntry("ToTarget", () -> toolHandler.setTool(new TrackToTargetTool()));
		this.addEntry("Remove", () -> toolHandler.setTool(new RemoveTrackTool()));
		addEntry("Back", () -> navigation.popMenu(this));
	}
}
