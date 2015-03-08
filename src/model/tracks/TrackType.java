package model.tracks;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public enum TrackType {

	LEFT, STRAIGHT, RIGHT;

	public TrackType next() {
		return values()[(this.ordinal() + 1) % values().length];
	}

	public TrackType prev() {
		return values()[(this.ordinal() - 1 + values().length) % values().length];
	}
}
