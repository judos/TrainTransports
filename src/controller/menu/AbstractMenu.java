package controller.menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import model.Constants;
import model.input.InputType;
import model.input.KeyEvent2;
import model.input.MouseEvent2;
import ch.judos.generic.data.geometry.PointI;
import controller.tools.ToolHandlerI;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class AbstractMenu implements MenuI {

	protected MenuNavigationI		navigation;
	protected ArrayList<MenuEntry>	entries;
	protected ToolHandlerI			toolHandler;

	public AbstractMenu(MenuNavigationI navigation, ToolHandlerI toolHandler) {
		this.entries = new ArrayList<MenuEntry>();
		this.toolHandler = toolHandler;
		this.navigation = navigation;
	}

	protected void addEntry(String title, Runnable runnable) {
		this.entries.add(new MenuEntry(title, runnable));
	}

	@Override
	public boolean handles(MouseEvent2 event) {
		if (event.getType() != InputType.PRESS || event.getButton() != MouseEvent.BUTTON1)
			return false;
		PointI point = event.getScreenPosition();
		for (int i = 0; i < this.entries.size(); i++) {
			MenuEntry m = this.entries.get(i);
			if (point.x >= 0 && point.x < m.width) {
				m.r.run();
				return true;
			}
			point.x -= m.width;
			if (point.x < 0)
				break;
		}

		return false;
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		int key = e.getKeyCode();
		if (e.getType() == InputType.PRESS && key >= KeyEvent.VK_1
			&& key <= KeyEvent.VK_9) {
			int nrPressedS0 = key - KeyEvent.VK_1;
			if (nrPressedS0 < this.entries.size()) {
				this.entries.get(nrPressedS0).r.run();
				return true;
			}
		}
		if (e.getType() == InputType.PRESS && key == KeyEvent.VK_ESCAPE) {
			this.navigation.popMenu(this);
			return true;
		}
		return false;
	}

	@Override
	public void paint(Graphics2D g, PointI mouse) {
		Rectangle clip = g.getClipBounds();
		g.setStroke(new BasicStroke());
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, clip.width - 1, clip.height - 1);
		g.setColor(Color.BLACK);
		g.setFont(Constants.FONT_MENU);
		int x = 0;
		for (int i = 0; i < this.entries.size(); i++) {
			MenuEntry m = this.entries.get(i);
			m.width = g.getFontMetrics().stringWidth(m.title) + 20;

			if (mouse != null && mouse.x >= x && mouse.x < x + m.width) {
				g.setColor(Color.GRAY);
				g.fillRect(x, 0, m.width, clip.height);
				g.setColor(Color.BLACK);
			}

			g.drawString(m.title, x + 10, clip.height - 10);
			x += m.width;
		}

		g.setColor(Color.BLACK);
		g.drawRect(0, 0, clip.width - 1, clip.height - 1);
	}

	class MenuEntry {
		Runnable	r;
		String		title;
		public int	width;

		public MenuEntry(String title, Runnable r) {
			this.title = title;
			this.r = r;
		}
	}

}