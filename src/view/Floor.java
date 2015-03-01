package view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ch.judos.generic.graphics.ImageUtils;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class Floor implements DrawableLayered {

	private BufferedImage	grass;
	private int				grassW;
	private int				grassH;

	public Floor() {
		this.grass = ImageUtils.loadBufferedImage("data/grass.png");
		this.grassW = this.grass.getWidth();
		this.grassH = this.grass.getHeight();
	}

	@Override
	public void paint(Graphics2D g, int layer) {
		if (layer == 0) {
			Rectangle clip = g.getClipBounds();

			int startX = clip.x - clip.x % this.grassW;
			if (clip.x < 0)
				startX -= this.grassW;
			int startY = clip.y - clip.y % this.grassH;
			if (clip.y < 0)
				startY -= this.grassH;

			for (int x = startX; x - startX < clip.width + this.grassW; x += this.grassW) {
				for (int y = startY; y - startY < clip.height + this.grassH; y += this.grassH) {
					g.drawImage(this.grass, x, y, null);
				}
			}
		}
	}

}
