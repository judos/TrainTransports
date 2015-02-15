package controller.tools;

import java.awt.Graphics2D;

import model.input.KeyEvent2;
import model.input.MouseEvent2;
import model.map.Map;

/**
 * @since 15.02.2015
 * @author Julian Schelker
 */
public class TrackToTargetTool implements ToolI {

	private Map	map;

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

	@Override
	public void dispose() {

	}

	@Override
	public void initialize(Map map) {
		this.map = map;
	}

	@Override
	public void drawAbsolute(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawInMap(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInInitialState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInitialState() {
		// TODO Auto-generated method stub

	}

}
