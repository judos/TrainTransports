package controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import model.map.Scroll;
import view.Gui;
import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class DrawableManager implements Drawable2d {

	private SimpleList<Drawable2d>	drawables;
	private Gui						window;
	private Scroll					scroll;

	public DrawableManager(Gui w) {
		this.drawables = new SimpleList<Drawable2d>();
		this.window = w;
		this.window.setDrawable(this);
	}

	@Override
	public void paint(Graphics2D g) {

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_SPEED);

		AffineTransform original = g.getTransform();
		if (this.scroll != null)
			this.scroll.updateAndTransform(g);

		for (Drawable2d draw : this.drawables) {
			draw.paint(g);
			g.setTransform(original);
		}
	}

	public void addDrawable(Drawable2d m) {
		this.drawables.add(m);
	}

	public Dimension getViewSize() {
		return this.window.getSize();
	}

	public void setScrollObject(Scroll s) {
		this.scroll = s;
	}

	public Component getComponent() {
		return this.window.getComponent();
	}

}
