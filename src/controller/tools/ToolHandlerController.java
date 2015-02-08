package controller.tools;

import java.awt.Graphics2D;

import model.input.KeyEvent2;
import model.input.KeyHandler;
import model.input.MouseEvent2;
import model.input.MouseHandler;
import model.map.Map;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class ToolHandlerController implements ToolHandlerI, Drawable2d, MouseHandler,
		KeyHandler {
	private ToolI	currentTool;
	private Map		map;

	public ToolHandlerController(Map map) {
		this.map = map;
	}

	@Override
	public void setTool(ToolI t) {
		if (this.currentTool != null)
			this.currentTool.dispose();
		this.currentTool = t;
		this.currentTool.initialize(this.map);
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
		if (this.currentTool != null)
			return this.currentTool.handles(e);
		return false;
	}

}
