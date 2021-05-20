import java.util.HashMap;
import java.util.LinkedList;

public class Main {
	public static void main(String args[]) {
		Loader Load = new Loader(140, 270);
//		Loader Load = new Loader(1000, 1000);
		Load.populateData("StateParkList");
		HashMap<String, MapNode> map = Load.createLocations();
		Load.autoGenNeighbours(1.0, map);
		for(MapNode mp : map.values()) {
			System.out.println(mp.toString() + " with neighbors: " + mp.getNeighbors().toString());
		}
//		AStarSearch search = new AStarSearch(map);
//		MapNode st = map.get("Indiana Dunes");
//		MapNode go = map.get("Brown County");
//		LinkedList<MapNode> result = search.search(st, go);
//		System.out.println(result);
	}
}
