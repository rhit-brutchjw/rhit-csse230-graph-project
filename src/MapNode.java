import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class MapNode {
	private int xPos;
	private int yPos;
	private int distToStart;
	private double timeToStart;
	private double latitude;
	private double longitude;
	public String name;
	private HashSet<MapNode> neighbors = new HashSet<MapNode>();
	public HashMap<MapNode, RoadPath> neighborToRoad;

	public MapNode(int x, int y, String name, double lon, double lat) {
		this.xPos = x;
		this.yPos = y;
		this.name = name;
		neighborToRoad = new HashMap<MapNode, RoadPath>();
		this.latitude = lat;
		this.longitude = lon;
	}

	public int getDist() {
		return distToStart;
	}

	public double getTime() {
		return timeToStart;
	}

	public double getlon() {
		return this.longitude;
	}

	public double getlat() {
		return this.latitude;
	}

	public int getx() {
		return this.xPos;
	}

	public int gety() {
		return this.yPos;
	}

	public void setDist(int newDist) {
		distToStart = newDist;
	}

	public void setTime(double totalTime) {
		timeToStart = totalTime;
	}

	public HashSet<MapNode> getNeighbors() {
		return neighbors;
	}

	public int dist(MapNode g) {
		return (int) Math.abs(Math.round(Math.sqrt(Math.pow(g.xPos - xPos, 2) + Math.pow(g.yPos - yPos, 2))));
	}

	public int time(MapNode g) {
		return dist(g) / 60;
	}

	public int calcDist(MapNode neighbor) {
		return neighborToRoad.get(neighbor).getLength();
	}

	public double calcTime(MapNode neighbor) {
		return neighborToRoad.get(neighbor).getTime();
	}

	public String toString() {
		return this.name;
	}

	public void addNeighbour(MapNode toAdd) {
		this.neighbors.add(toAdd);
	}

	public void calcAllDist() {
		for (MapNode n : this.neighbors) {
			int dist = this.dist(n);
			Random r = new Random();
			neighborToRoad.put(n, new RoadPath(this, n, 60.0, dist));
		}
	}
}
