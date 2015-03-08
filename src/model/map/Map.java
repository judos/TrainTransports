package model.map;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import model.tracks.Track;
import model.tracks.builders.TrackBuildConstraint;
import view.Floor;
import ch.judos.generic.data.concurrent.SimpleList;
import ch.judos.generic.data.geometry.DirectedPoint;
import ch.judos.generic.data.geometry.PointI;
import ch.judos.generic.data.serialization.RStorable;
import ch.judos.generic.graphics.Drawable2d;

/**
 * @since 28.01.2015
 * @author Julian Schelker
 */
public class Map implements Drawable2d, RStorable {

	// TODO: improve performance by using hashset
	private SimpleList<Track>	tracks;
	private transient Floor		floor;
	private boolean				drawConnections;

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
		if (this.drawConnections) {

			this.drawConnections = false;
		}
	}

	private void drawTracks(Graphics2D g, int layer) {
		// TODO: improve performance by drawing only visible tracks
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

	public List<TrackBuildConstraint> getTrackConnectionsFrom(PointI mapPosition) {
		// TODO: improve performance by checking only local tracks
		ArrayList<TrackBuildConstraint> result = new ArrayList<TrackBuildConstraint>();
		for (Track t : this.tracks) {
			for (DirectedPoint point : t.getMainConnections()) {
				if (point.getPoint().distance(mapPosition) < Track.sleeperLength / 2)
					result.add(new TrackBuildConstraint(point));
			}
		}
		return result;
	}

	public void drawConnections(Graphics2D g) {
		// TODO: improve performance: draw only visible connections
		for (Track t : this.tracks)
			t.paintConnections(g);
		this.drawConnections = true;
	}

	public List<Track> getTrackByPoint(PointI point) {
		// TODO: improve performance: check only local tracks
		ArrayList<Track> result = new ArrayList<Track>();
		if (point == null)
			return result;
		for (Track t : this.tracks) {
			if (t.contains(point))
				result.add(t);
		}
		return result;
	}

}
