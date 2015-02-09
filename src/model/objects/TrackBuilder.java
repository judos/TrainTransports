package model.objects;

import java.awt.Graphics2D;

import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 08.02.2015
 * @author Julian Schelker
 */
public abstract class TrackBuilder implements Drawable2d {

	public TrackBuilder() {

	}

	public abstract Track getTrack();

	public abstract void updateWithTarget(PointI mapTarget);

	public abstract boolean isValid();

	@Override
	public void paint(Graphics2D g) {
		this.getTrack().paint(g, 0);
		this.getTrack().paint(g, 1);
	}

	/**
	 * @return a newly initialized copy of the current track layout
	 */
	public Track getTrackNew() {
		return getTrack().copy();
	}
}
