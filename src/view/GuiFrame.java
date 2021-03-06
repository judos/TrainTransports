package view;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import model.input.InputProvider;
import ch.judos.generic.data.geometry.PointF;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 01.02.2015
 * @author Julian Schelker
 */
public class GuiFrame extends JFrame implements InputProvider {

	private Drawable2d			drawable;
	private static final long	serialVersionUID	= -2940010842021558595L;

	public GuiFrame(Drawable2d drawable) {
		super();
		this.drawable = drawable;
		this.setTitle("Sim Track");
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1920, 1080);
		this.setUndecorated(false);
		this.setAlwaysOnTop(false);
		this.setIgnoreRepaint(true);
		// this.setResizable(false);
		this.setFocusTraversalKeysEnabled(false);

		this.getContentPane().requestFocusInWindow();
		this.setVisible(true);
		this.createBufferStrategy(2);
	}

	public void renderScreen() {
		try {
			BufferStrategy strategy = getBufferStrategy();

			strategy.show();
			Toolkit.getDefaultToolkit().sync();

			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			if (!this.isUndecorated()) {
				PointF t = new PointF(this.getContentPane().getLocationOnScreen())
					.subtract(this.getLocationOnScreen());
				g.translate(t.x, t.y);
				Dimension size = this.getContentPane().getSize();
				g.setClip(0, 0, size.width, size.height);
			} else
				g.setClip(0, 0, getWidth(), getHeight());
			this.drawable.paint(g);
			g.dispose();

		} catch (IllegalStateException e) {
		}
	}

	@Override
	public Dimension getSize() {
		if (this.isUndecorated())
			return super.getSize();
		else
			return this.getContentPane().getSize();
	}
}
