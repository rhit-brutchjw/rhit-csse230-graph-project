import java.util.HashMap;
import java.util.HashSet;

public class MapNode {
	private int xPos;
	private int yPos;
	private int distToStart;
	private HashSet<MapNode> neighbors = new HashSet<MapNode>();
	private HashMap<MapNode, RoadPath> neighborToRoad;
	
	public MapNode(int x, int y, HashMap<MapNode, RoadPath> neighborToRoad) {
		xPos = x;
		yPos = y;
		this.neighborToRoad = neighborToRoad;
		for(MapNode neighbor : neighborToRoad.keySet()) {
			neighbors.add(neighbor);
		}
	}
	public MapNode(int x, int y) {
		this.xPos=x;
		this.yPos=y;
	}
	public int getDist() {
		return distToStart;
	}

	public void setDist(int newDist) {
		distToStart = newDist;
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
		return "xPos = " + this.xPos + "; yPos = " +yPos + " Neighbours: " + !this.neighbors.isEmpty();
	}
	public void addNeighbour(MapNode toAdd) {
		this.neighbors.add(toAdd);
	}
	public void calcAllDist() {
		for(MapNode n : this.neighbors) {
			int dist = (int) Math.sqrt((Math.pow(this.xPos-n.xPos,2) + Math.pow(this.yPos-n.yPos, 2)));
			neighborToRoad.put(this,new RoadPath("Temp",50,dist));
		}
	}
}
