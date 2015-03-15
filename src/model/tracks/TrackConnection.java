package model.tracks;

import ch.judos.generic.data.serialization.RStorable;

/**
 * @since 09.03.2015
 * @author Julian Schelker
 */
public class TrackConnection implements RStorable {

	public Track	connected;
	public int		directionOnConnectedTrack;

	/**
	 * used for RStorage
	 */
	@SuppressWarnings("unused")
	private TrackConnection() {

	}

	public TrackConnection(Track connected, int targetDirection) {
		this.connected = connected;
		this.directionOnConnectedTrack = targetDirection;
	}

	public double getPositionOnTargetTrack() {
		if (this.directionOnConnectedTrack == 0)
			return 0;
		else
			return this.connected.getTrackLength();
	}
}
