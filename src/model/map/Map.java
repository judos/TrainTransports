package model.map;

import java.awt.Graphics2D;
import java.awt.Point;

import model.TrackBuildConstraint;
import model.objects.Track;
import view.Floor;
import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class Map implements Drawable2d {

	private SimpleList<Track>	tracks;
	private Floor				floor;

	public Map() {
		this.tracks = new SimpleList<Track>();
		this.floor = new Floor();
	}

	@Override
	public void paint(Graphics2D g) {
		for (int layer = 0; layer <= 1; layer++) {
			this.floor.paint(g, layer);
			drawTracks(g, layer);
		}
	}

	private void drawTracks(Graphics2D g, int layer) {
		for (Track t : this.tracks) {
			t.paint(g, layer);
		}
	}

	public void addTrack(Track t) {
		this.tracks.add(t);
	}

	public void removeTrack(Track t) {
		this.tracks.remove(t);
	}

	public TrackBuildConstraint[] getTrackConnectionsFrom(Point mapPosition) {
		return null;
	}

}
