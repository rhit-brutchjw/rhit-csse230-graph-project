import java.util.HashMap;
import java.util.HashSet;

public class MapNode {
	private int xPos;
	private int yPos;
	private int distToStart;
	private HashSet<MapNode> neighbors;
	private HashMap<MapNode, RoadPath> neighborToRoad;
	
	public MapNode(int x, int y, HashMap<MapNode, RoadPath> neighborToRoad) {
		xPos = x;
		yPos = y;
		this.neighborToRoad = neighborToRoad;
		for(MapNode neighbor : neighborToRoad.keySet()) {
			neighbors.add(neighbor);
		}
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
}
