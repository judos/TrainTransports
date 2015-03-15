package model.tracks;

/**
 * @since 09.03.2015
 * @author Julian Schelker
 */
public class InvalidTrackReachedException extends Exception {

	private static final long	serialVersionUID	= 3596405200247739950L;

	public InvalidTrackReachedException(Track actual, Track expected) {
		super("During movement the track " + actual
			+ " was reached instead of the expected " + expected
			+ ": check your path-finding!");
	}
}
