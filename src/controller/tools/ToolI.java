package controller.tools;

import java.awt.Graphics2D;

import model.input.KeyHandler;
import model.input.MouseHandler;
import model.map.Map;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public interface ToolI extends MouseHandler, KeyHandler {

	/**
	 * free all ressources that are used by the tool, and prepare to be unloaded
	 */
	public void dispose();

	public void initialize(Map map);

	/**
	 * this is drawn over the content that is drawn in drawInMap(g)
	 * 
	 * @param g
	 */
	public void drawAbsolute(Graphics2D g);

	public void drawInMap(Graphics2D g);

}
