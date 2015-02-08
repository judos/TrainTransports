package controller.menu;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import model.input.InputType;
import model.input.KeyEvent2;
import model.input.MouseEvent2;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class BuildMenu implements MenuI {

	private MenuNavigationI	navigation;

	public BuildMenu(MenuNavigationI navigation) {
		this.navigation = navigation;
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getType() == InputType.PRESS && e.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.navigation.popMenu(this);
		return false;
	}

	@Override
	public void paint(Graphics2D g) {

	}

}
