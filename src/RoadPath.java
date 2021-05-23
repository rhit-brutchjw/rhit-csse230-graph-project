
public class RoadPath {
	private MapNode to;
	private MapNode from;
	private double MPH;
	private int length;

	public RoadPath(MapNode to, MapNode from, double MPH, int length) {
		this.to = to;
		this.from = from;
		this.MPH = MPH;
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public double getTime() {
		return length / MPH;
	}

	public MapNode getTo() {
		return to;
	}

	public MapNode getFrom() {
		return from;
	}
}
