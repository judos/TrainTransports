package controller.menu;

import java.awt.Graphics2D;

import model.input.KeyHandler;
import model.input.MouseHandler;
import ch.judos.generic.data.geometry.PointI;

/**
 * @since 07.02.2015
 * @author Julian Schelker
 */
public interface MenuI extends MouseHandler, KeyHandler {

	public void paint(Graphics2D g, PointI mouse);

}
