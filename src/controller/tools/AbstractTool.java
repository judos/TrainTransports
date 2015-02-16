package controller.tools;

import java.awt.Graphics2D;

import model.input.KeyHandler;
import model.input.MouseHandler;
import model.map.Map;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public abstract class AbstractTool implements MouseHandler, KeyHandler {

	enum State {
		// the tool is ready and no input is processed yet
		READY,
		// the first connection has already been chosen
		STARTED;
	}

	protected State	state;

	/**
	 * free all ressources that are used by the tool, and prepare to be unloaded
	 */
	public abstract void dispose();

	public abstract void initialize(Map map);

	public boolean isInInitialState() {
		return this.state == State.READY;
	}

	public void setInitialState() {
		this.state = State.READY;
	}

	/**
	 * this is drawn over the content that is drawn in drawInMap(g)
	 * 
	 * @param g
	 */
	public abstract void drawAbsolute(Graphics2D g);

	public abstract void drawInMap(Graphics2D g);

}
