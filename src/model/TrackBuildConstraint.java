package model;

import java.util.List;

import ch.judos.generic.data.geometry.DirectedPoint;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public class TrackBuildConstraint {

	private DirectedPoint	dirPoint;
	private List<TrackType>	allowedDirection;

	public TrackBuildConstraint(DirectedPoint d) {
		this.dirPoint = d;
	}

	public boolean isDirectionValid(TrackType type) {
		// TODO: implement
		return true;
	}

	/**
	 * @return the dirPoint
	 */
	public DirectedPoint getDirPoint() {
		return dirPoint;
	}
}
