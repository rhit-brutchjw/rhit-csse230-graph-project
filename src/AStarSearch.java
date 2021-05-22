import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * 
 * @author brutchjw, rameydj, grimmbed
 * 
 */
public class AStarSearch {
	public HashMap<String, MapNode> currentMap;

	public AStarSearch(HashMap<String, MapNode> map) {
		currentMap = map;
	}

	public LinkedList<MapNode> search(MapNode start, MapNode goal) {
		// sets up the parent map, used for recreating the shortest path
		HashMap<MapNode, MapNode> parents = new HashMap<MapNode, MapNode>();
		// sets up the set of already visited nodes
		HashSet<MapNode> visited = new HashSet<MapNode>();
		// sets up the map of distances from start to the goal
		// initializes the distances as null to start
		HashMap<MapNode, Integer> distances = initDist();

		// creates the priority queue used in the search algorithm
		PriorityQueue<MapNode> queue = initQueue();

		// sets the starting nodes distance as 0
		start.setDist(0);
		// updates the distance from the start to the goal
		distances.put(start, 0);
		// adds the starting node to the priority queue
		queue.add(start);

		MapNode current = null;

		while (!queue.isEmpty()) {
			// takes the next node with the smallest distance to the goal so far
			current = queue.poll();
			if (!visited.contains(current)) {
				// adds current to the set of already visited nodes
				visited.add(current);
				// if the goal is found a path is recreated based on the parent link
				if (current.equals(goal)) {
					return createPath(start, goal, parents);
				}
				HashSet<MapNode> neighbors = current.getNeighbors();
				// runs through and updates all the distances for each neighbor
				for (MapNode n : neighbors) {
					if (!visited.contains(n)) {
						// calculates straight line distance from the neighbor to the goal
						int straightDist = n.dist(goal);
						// calculates distance from the current node to the neighbor based on the road
						int neighborDistance = current.calcDist(n);
						// calculates the total distance from start to finish for use in the priority
						// queue
						int totalDistance = current.getDist() + neighborDistance + straightDist;
						// if the calculated total distance is less than the current total distance it:
						if (distances.get(n) == null || totalDistance < distances.get(n)) {
							// updates the current total distance
							distances.put(n, totalDistance);
							// sets the distance value used in the compare method of the priority queue
							n.setDist(totalDistance);
							// creates a link from neighbor to current where current is the parent
							// used to recreate the shortest path
							parents.put(n, current);
							// adds the neighbor to the queue
							queue.add(n);
						}
					}
				}
			}
		}
		return null;
	}
	
	public LinkedList<MapNode> searchTime(MapNode start, MapNode goal) {
		HashMap<MapNode, MapNode> parents = new HashMap<MapNode, MapNode>();
		HashSet<MapNode> visited = new HashSet<MapNode>();
		HashMap<MapNode, Integer> times = initDist();
		PriorityQueue<MapNode> queue = initQueue();
		start.setTime(0);
		times.put(start, 0);
		queue.add(start);
		MapNode current = null;
		while (!queue.isEmpty()) {
			current = queue.poll();
			if (!visited.contains(current)) {
				visited.add(current);
				if (current.equals(goal)) {
					return createPath(start, goal, parents);
				}
				HashSet<MapNode> neighbors = current.getNeighbors();
				for (MapNode n : neighbors) {
					if (!visited.contains(n)) {
						int straightTime = n.time(goal);
						int neighborTime = current.calcTime(n);
						int totalTime = current.getTime() + neighborTime + straightTime;
						if (times.get(n) == null || totalTime < times.get(n)) {
							times.put(n, totalTime);
							n.setTime(totalTime);
							parents.put(n, current);
							queue.add(n);
						}
					}
				}
			}
		}
		return null;
	}
	
	public int getFinalDist(LinkedList<MapNode> result) {
		int d = 0;
		for(int i = 0; i < result.size() - 1; i++) {
			MapNode m1 = result.get(i);
			MapNode m2 = result.get(i+1);
			d += haversineFormula(m1.getlat(), m1.getlon(), m2.getlat(), m2.getlon());
		}
		return d;
	}
	
	public int getFinalTime(LinkedList<MapNode> result) {
		int t = 0;
		for(int i = 0; i < result.size() - 1; i++) {
			MapNode m1 = result.get(i);
			MapNode m2 = result.get(i+1);
			int d = haversineFormula(m1.getlat(), m1.getlon(), m2.getlat(), m2.getlon());
			int r = m1.neighborToRoad.get(m2).getTime();
			t += d/r;
		}
		return t;
	}
	
	private int haversineFormula(double lat1, double lon1, double lat2, double lon2) {
		int R = 6371000;
		double phi1 = lat1 * Math.PI / 180;
		double phi2 = lat2 * Math.PI / 180;
		double deltaPhi = (lat2-lat1) * Math.PI / 180;
		double deltaLambda = (lon2 - lon1) * Math.PI / 180;
		double a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R * c;
		int miles = (int) Math.round(d / 1609.34);
		return miles;
		
	}

	private LinkedList<MapNode> createPath(MapNode startNode, MapNode goalNode, HashMap<MapNode, MapNode> parents) {
		LinkedList<MapNode> bestPath = new LinkedList<MapNode>();
		MapNode current = goalNode;
		while (!current.equals(startNode)) {
			bestPath.addFirst(current);
			current = parents.get(current);
		}
		bestPath.addFirst(startNode);
		return bestPath;
	}

	private PriorityQueue<MapNode> initQueue() {
		return new PriorityQueue<MapNode>(20, new Comparator<MapNode>() {
			public int compare(MapNode x, MapNode y) {
				if (x.getDist() < y.getDist()) {
					return -1;
				}
				if (x.getDist() > y.getDist()) {
					return 1;
				}
				return 0;
			};
		});
	}

	private HashMap<MapNode, Integer> initDist() {
		HashMap<MapNode, Integer> distances = new HashMap<MapNode, Integer>();
		Iterator<MapNode> iter = currentMap.values().iterator();
		while (iter.hasNext()) {
			MapNode node = iter.next();
			distances.put(node, null);
		}
		return distances;
	}
}
