package controller;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Stack;

import model.input.KeyEvent2;
import model.input.KeyHandler;
import model.input.MouseEvent2;
import model.input.MouseHandler;
import ch.judos.generic.graphics.Drawable2d;
import controller.menu.MainMenu;
import controller.menu.MenuI;
import controller.menu.MenuNavigationI;

/**
 * @since 07.02.2015
 * @author Julian Schelker
 */
public class MenuController implements Drawable2d, MouseHandler, KeyHandler,
		MenuNavigationI {

	public static final int	MENU_WIDTH	= 600;
	public static final int	MENU_HEIGHT	= 100;

	private Stack<MenuI>	menuStack;

	public MenuController(InputManager mm) {
		this.menuStack = new Stack<MenuI>();
		this.menuStack.push(new MainMenu(this));
		mm.addFirst((MouseHandler) this);
		mm.addFirst((KeyHandler) this);
	}

	@Override
	public void paint(Graphics2D g) {
		Rectangle clip = g.getClipBounds();
		AffineTransform t = g.getTransform();
		g.translate(clip.width / 2 - MENU_WIDTH / 2, clip.height - MENU_HEIGHT);
		g.setClip(0, 0, MENU_WIDTH, MENU_HEIGHT);
		this.menuStack.peek().paint(g);
		g.setTransform(t);
		g.setClip(clip);
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		return this.menuStack.peek().handles(m);
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		return this.menuStack.peek().handles(e);
	}

	@Override
	public void pushMenu(MenuI menu) {
		this.menuStack.push(menu);
	}

	@Override
	public void popMenu(MenuI menu) {
		if (this.menuStack.peek() == menu)
			this.menuStack.pop();
	}

}
