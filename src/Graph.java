import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
	public HashMap<String, MapNode> locations;
	public HashMap<MapNode, ArrayList<RoadPath>> roadsToNeighbors;
}
