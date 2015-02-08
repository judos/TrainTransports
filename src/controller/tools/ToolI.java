package controller.tools;

import model.input.KeyHandler;
import model.input.MouseHandler;
import model.map.Map;
import model.map.Scroll;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public interface ToolI extends Drawable2d, MouseHandler, KeyHandler {

	/**
	 * free all ressources that are used by the tool, and prepare to be unloaded
	 */
	public void dispose();

	public void initialize(Map map, Scroll scroll);

}
