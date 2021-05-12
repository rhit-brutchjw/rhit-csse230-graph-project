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
						// calculates the total distance from start to finish for use in the priority queue
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
