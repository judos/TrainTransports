package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import model.input.InputProvider;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 27.01.2015
 * @author Julian Schelker
 */
public class Gui implements Drawable2d {

	private GuiFrame	frame;
	private Timer		timer;
	private Drawable2d	drawable;

	public Gui(Optional<Runnable> onQuit) {
		this.frame = new GuiFrame(this);
		if (onQuit.isPresent()) {
			this.frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					onQuit.get().run();
					System.out.println(e);
				}
			});
		}
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
						lastFrame = ns;
						try {
							frame.renderScreen();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						long remaining = lastFrame + delay - System.nanoTime() - 1000000;
						if (remaining > 0)
							Thread
								.sleep(remaining / 1000000, (int) (remaining % 1000000));
					} catch (InterruptedException e) {
					}
				}
			}
		};
		t.setPriority(Thread.MAX_PRIORITY);
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

	public InputProvider getInputProvider() {
		return new InputProvider() {
			@Override
			public void addMouseListener(MouseListener m) {
				frame.getContentPane().addMouseListener(m);
			}

			@Override
			public void addKeyListener(KeyListener k) {
				frame.addKeyListener(k);
			}

			@Override
			public void addMouseWheelListener(MouseWheelListener m) {
				frame.getContentPane().addMouseWheelListener(m);
			}

		};
	}

	@Override
	public void paint(Graphics2D g) {
		Rectangle size = g.getClipBounds();
		g.clearRect(0, 0, size.width, size.height);
		if (this.drawable != null)
			this.drawable.paint(g);
	}

	public Component getComponent() {
		return this.frame.getContentPane();
	}

	public void quit() {
		this.frame.setVisible(false);
		this.frame.dispose();
	}

}
