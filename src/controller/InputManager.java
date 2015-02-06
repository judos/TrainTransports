package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import model.input.InputType;
import model.input.KeyEvent2;
import model.input.KeyHandler;
import model.input.MouseEvent2;
import model.input.MouseHandler;
import model.input.MouseWheelEvent2;
import model.input.MouseWheelHandler;
import model.map.Scroll;
import view.Gui;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class InputManager implements MouseListener, KeyListener, MouseWheelListener {

	/**
	 * if this is set to true, an Listener handling an event will break the chain, and no further
	 * objects will receive the same event
	 */
	public static boolean					breakChain	= false;

	private Gui								window;
	private ArrayList<MouseHandler>			listeners;
	private ArrayList<KeyHandler>			listenersKey;
	private ArrayList<MouseWheelHandler>	mouseWheel;

	private Scroll							scroll;

	public InputManager(Gui w) {
		this.window = w;
		this.window.getFrame().addMouseListener(this);
		this.window.getFrame().addKeyListener(this);
		this.window.getFrame().addMouseWheelListener(this);
		this.listeners = new ArrayList<MouseHandler>();
		this.listenersKey = new ArrayList<KeyHandler>();
		this.mouseWheel = new ArrayList<MouseWheelHandler>();
	}

	public void add(MouseWheelHandler handler) {
		this.mouseWheel.add(handler);
	}

	public void addFirst(MouseHandler handler) {
		this.listeners.add(0, handler);
	}

	public void addLast(MouseHandler handler) {
		this.listeners.add(handler);
	}

	public void remove(MouseHandler handler) {
		this.listeners.remove(handler);
	}

	public void addFirst(KeyHandler handler) {
		this.listenersKey.add(0, handler);
	}

	public void addLast(KeyHandler handler) {
		this.listenersKey.add(handler);
	}

	public void remove(KeyHandler handler) {
		this.listenersKey.remove(handler);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		handle(new MouseEvent2(InputType.PRESSED_AND_RELEASED, e));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		handle(new MouseEvent2(InputType.PRESS, e));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		handle(new MouseEvent2(InputType.RELEASE, e));
	}

	protected void handle(MouseEvent2 e) {
		for (MouseHandler h : this.listeners) {
			if (h.handles(e) && breakChain)
				break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		KeyEvent2 e2 = new KeyEvent2(InputType.PRESSED_AND_RELEASED, e);
		for (KeyHandler h : this.listenersKey) {
			if (h.handles(e2))
				break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		KeyEvent2 e2 = new KeyEvent2(InputType.PRESS, e);
		for (KeyHandler h : this.listenersKey) {
			if (h.handles(e2))
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		KeyEvent2 e2 = new KeyEvent2(InputType.RELEASE, e);
		for (KeyHandler h : this.listenersKey) {
			if (h.handles(e2))
				break;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		MouseWheelEvent2 e2 = new MouseWheelEvent2(e);
		for (MouseWheelHandler h : this.mouseWheel) {
			if (h.handles(e2))
				break;
		}
	}

	public void setScrollObject(Scroll s) {
		this.scroll = s;
	}

}
