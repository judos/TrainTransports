package controller.tools;

import java.awt.Graphics2D;

import model.input.KeyEvent2;
import model.input.MouseEvent2;
import model.map.Map;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class BuildSimpleTrackTool implements ToolI {

	private Map	map;

	enum State {
		// the tool is ready and no input is processed yet
		READY,
		// the first connection has already been chosen
		STARTED;
	}

	@Override
	public void initialize(Map map) {
		this.map = map;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handles(MouseEvent2 m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		// TODO Auto-generated method stub
		return false;
	}

}
