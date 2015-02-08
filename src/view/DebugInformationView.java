package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import model.input.InputType;
import model.input.KeyEvent2;
import model.input.KeyHandler;
import ch.judos.generic.control.ActionCounter;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 31.01.2015
 * @author Julian Schelker
 */
public class DebugInformationView implements Drawable2d, KeyHandler {

	private boolean			displayed;
	private ActionCounter	fps;
	private Font			font;
	private boolean			fpsOn;

	public DebugInformationView() {
		this.displayed = true;
		this.fps = new ActionCounter();
		this.fpsOn = false;
		this.font = new Font("Arial", 0, 20);
	}

	@Override
	public void paint(Graphics2D g) {
		if (this.fps.action())
			this.fpsOn = true;
		if (!this.displayed)
			return;
		if (this.fpsOn) {
			g.setColor(Color.black);
			g.setFont(this.font);
			// AlphaComposite com =
			// AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			// g.setComposite(com);
			g.drawString("fps: " + this.fps.getActionRate(), 10, 30);
			g.setColor(Color.white);

			g.drawString("fps: " + this.fps.getActionRate(), 12, 32);

		}
	}

	@Override
	public boolean handles(KeyEvent2 e) {
		if (e.getType() == InputType.PRESS && e.getKeyCode() == KeyEvent.VK_F3)
			this.displayed = !this.displayed;
		return false;
	}

}
