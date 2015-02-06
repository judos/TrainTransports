package view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import ch.judos.generic.graphics.Drawable2d;
import ch.judos.generic.graphics.fullscreen.FullScreenOrWindowed;

/**
 * @since 27.01.2015
 * @author Julian Schelker
 */
public class Gui implements Drawable2d {

	private GuiFrame				frame;
	private Timer					timer;
	private Drawable2d				drawable;
	private FullScreenOrWindowed	fs;

	public Gui() {
		this.frame = new GuiFrame(this);
	}

	public void startViewTimer(int fps) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				frame.renderScreen();
			}
		};
		this.timer = new Timer("Render thread", false);
		int delay = (int) (1000. / fps);
		this.timer.scheduleAtFixedRate(task, delay, delay);
	}

	public void startView(int fps) {
		Thread t = new Thread("Render thread") {
			@Override
			public void run() {
				long delay = 1000000000 / fps;
				long lastFrame = System.nanoTime();
				while (true) {
					long ns = System.nanoTime();
					if (ns - lastFrame >= delay) {
						frame.renderScreen();
						lastFrame += delay;
					}
					try {
						int remaining = (int) (System.nanoTime() - ns - delay);
						if (remaining > 1000)
							Thread.sleep(remaining / 1000000, remaining % 1000000);
					} catch (InterruptedException e) {}
				}
			}
		};
		t.start();
	}

	public void startViewWhileTrue() {
		Thread t = new Thread("Render thread") {
			@Override
			public void run() {
				while (true)
					frame.renderScreen();
			}
		};
		t.start();
	}

	public void setDrawable(Drawable2d m) {
		this.drawable = m;
	}

	public Dimension getSize() {
		return this.frame.getSize();
	}

	public Frame getFrame() {
		return this.frame;
	}

	@Override
	public void paint(Graphics2D g) {
		Rectangle size = g.getClipBounds();
		g.clearRect(0, 0, size.width, size.height);
		if (this.drawable != null)
			this.drawable.paint(g);
	}

}
