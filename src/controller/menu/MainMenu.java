package controller.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import model.Constants;
import model.input.InputType;
import model.input.KeyEvent2;
import model.input.MouseEvent2;

/**
 * @since 07.02.2015
 * @author Julian Schelker
 */
public class MainMenu implements MenuI {

	private MenuNavigationI	navigation;

	public MainMenu(MenuNavigationI navigation) {
		this.navigation = navigation;
	}

	@Override
	public boolean handles(MouseEvent2 m) {
		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getType() == InputType.PRESS && e.getKeyCode() == KeyEvent.VK_1)
			this.navigation.pushMenu(new BuildMenu(this.navigation));
		return false;
	}

	@Override
	public void paint(Graphics2D g) {
		Rectangle clip = g.getClipBounds();

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, clip.width, clip.height);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, clip.width, clip.height);

		g.setFont(Constants.FONT_MENU);
		g.drawString("Build Tracks", 10, clip.height - 20);
	}

}
