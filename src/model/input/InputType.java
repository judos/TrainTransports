package model.input;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public enum InputType {

	PRESSED_AND_RELEASED(false), PRESS(true), RELEASE(false);

	protected boolean	pressed;

	private InputType(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean isPressed() {
		return this.pressed;
	}
}
