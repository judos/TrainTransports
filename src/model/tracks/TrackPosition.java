package model.tracks;

import ch.judos.generic.data.DynamicList;
import ch.judos.generic.data.serialization.RStorable;

/**
 * @since 09.03.2015
 * @author Julian Schelker
 */
public class TrackPosition implements RStorable {

	/**
	 * current track
	 */
	public Track	track;
	/**
	 * track index position [0,trackLength] between the connection points
	 */
	public double	position;
	/**
	 * defines the index of the track connection point from where the position
	 * came from
	 */
	public int		direction;

	/**
	 * used for RStorage
	 */
	@SuppressWarnings("unused")
	private TrackPosition() {
	}

	public TrackPosition(Track track, double position, int direction) {
		this.track = track;
		this.position = position;
		this.direction = direction;
	}

	/**
	 * @param speed
	 *            movement speed for this update (positive or negative)
	 * @param targetTracks
	 *            the next tracks to be reached
	 * @return the number of tracks in the targetTracks list that were reached
	 * @throws InvalidTrackReachedException
	 */
	public int move(double speed, DynamicList<Track> targetTracks)
		throws InvalidTrackReachedException {
		int targetTrackIndex = 0;
		if (this.track == targetTracks.getOrNull(targetTrackIndex))
			targetTrackIndex++;
		double remainingSpeed = speed;
		while (remainingSpeed != 0) {
			// calculate remaining speed after move on track
			remainingSpeed = this.track.positionalMove(this, remainingSpeed, targetTracks
				.getOrNull(targetTrackIndex));
			if (this.track == targetTracks.getOrNull(targetTrackIndex))
				targetTrackIndex++;
		}
		return targetTrackIndex;
	}
}
