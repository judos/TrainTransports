package model.input;

import java.awt.event.KeyEvent;

/**
 * @since 31.01.2015
 * @author Julian Schelker
 */
public class KeyEvent2 {
	private int			keyCode;
	private InputType	type;

	public KeyEvent2(InputType type, KeyEvent e) {
		this.type = type;
		this.keyCode = e.getKeyCode();
	}

	/**
	 * @return the keyCode
	 */
	public int getKeyCode() {
		return keyCode;
	}

	/**
	 * @return the type
	 */
	public InputType getType() {
		return type;
	}

}
