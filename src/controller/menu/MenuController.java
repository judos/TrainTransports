package controller.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Stack;

import model.Mouse;
import model.input.KeyEvent2;
import model.input.KeyHandler;
import model.input.MouseEvent2;
import model.input.MouseHandler;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.graphics.Drawable2d;
import controller.tools.ToolHandlerI;

/**
 * @since 07.02.2015
 * @author Julian Schelker
 */
public class MenuController implements Drawable2d, MouseHandler, KeyHandler,
	MenuNavigationI {

	public static final int	MENU_WIDTH	= 600;
	public static final int	MENU_HEIGHT	= 100;

	private Stack<MenuI>	menuStack;
	private int				dx;
	private int				dy;
	private Runnable		onQuit;

	public MenuController(ToolHandlerI toolHandler, Runnable onQuit) {
		this.menuStack = new Stack<MenuI>();
		this.menuStack.push(new MainMenu(this, toolHandler));
		this.onQuit = onQuit;
	}

	@Override
	public void paint(Graphics2D g) {
		Rectangle clip = g.getClipBounds();
		AffineTransform t = g.getTransform();
		this.dx = clip.width / 2 - MENU_WIDTH / 2;
		this.dy = clip.height - MENU_HEIGHT;

		g.translate(dx, dy);
		PointI p = Mouse.getMousePoint();
		if (p != null) {
			p.translate(-dx, -dy);
			if (!p.inRectFromZero(MENU_WIDTH, MENU_HEIGHT))
				p = null;
		}
		g.setClip(0, 0, MENU_WIDTH, MENU_HEIGHT);
		this.menuStack.peek().paint(g, p);
		g.setTransform(t);
		g.setClip(clip);
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		MouseEvent2 copy = m.deepCopy();
		copy.getScreenPosition().translate(-dx, -dy);
		Shape menuShape = new Rectangle(0, 0, MenuController.MENU_WIDTH,
			MenuController.MENU_HEIGHT);
		if (!menuShape.contains(copy.getScreenPosition()))
			return false;
		return this.menuStack.peek().handles(copy);
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
		if (menu instanceof MainMenu) {
			quit();
			return;
		}
		if (this.menuStack.peek() == menu)
			this.menuStack.pop();
	}

	@Override
	public void quit() {
		this.onQuit.run();
	}

}
