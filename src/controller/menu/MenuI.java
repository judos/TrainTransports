package controller.menu;

import java.awt.Graphics2D;
import java.awt.Point;

import model.input.KeyHandler;
import model.input.MouseHandler;

/**
 * @since 07.02.2015
 * @author Julian Schelker
 */
public interface MenuI extends MouseHandler, KeyHandler {

	public void paint(Graphics2D g, Point mouse);

}
