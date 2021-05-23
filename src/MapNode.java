import java.util.HashMap;
import java.util.HashSet;

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
		return haversineFormula(g.latitude, g.longitude);
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

	private int haversineFormula(double lat2, double lon2) {
		int R = 6371000;
		double phi1 = this.latitude * Math.PI / 180;
		double phi2 = lat2 * Math.PI / 180;
		double deltaPhi = (lat2 - this.latitude) * Math.PI / 180;
		double deltaLambda = (lon2 - this.longitude) * Math.PI / 180;
		double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2)
				+ Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		int miles = (int) Math.round(d / 1609.34);
		return miles;

	}
}
