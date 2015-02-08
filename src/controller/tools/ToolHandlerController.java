package controller.tools;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import model.input.InputType;
import model.input.KeyEvent2;
import model.input.KeyHandler;
import model.input.MouseEvent2;
import model.input.MouseHandler;
import model.map.Map;
import model.map.Scroll;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class ToolHandlerController implements ToolHandlerI, Drawable2d, MouseHandler,
		KeyHandler {
	private ToolI	currentTool;
	private Map		map;
	private Scroll	scroll;

	public ToolHandlerController(Map map, Scroll scroll) {
		this.map = map;
		this.scroll = scroll;
	}

	@Override
	public void setTool(ToolI t) {
		if (this.currentTool != null)
			this.currentTool.dispose();
		this.currentTool = t;
		if (this.currentTool != null)
			this.currentTool.initialize(this.map, this.scroll);
	}

	@Override
	public void paint(Graphics2D g) {
		if (this.currentTool != null)
			this.currentTool.paint(g);
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		if (this.currentTool != null)
			return this.currentTool.handles(m);
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getType() == InputType.PRESS && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (this.currentTool != null) {
				setTool(null);
				return true;
			}
		}
		if (this.currentTool != null)
			return this.currentTool.handles(e);
		return false;
	}

}
