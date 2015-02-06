package view;

import java.awt.Graphics2D;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public interface DrawableLayered {
	public void paint(Graphics2D g, int layer);
}
