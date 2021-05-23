import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class TestingMain {
	public static void main(String args[]) {
		Loader Load = new Loader(1000, 1000);
		Load.populateData("StateParkList");
		HashMap<String, MapNode> map = Load.createLocations();
		Load.autoGenNeighbours(1.0, map);
		AStarSearch search = new AStarSearch(map);
		ArrayList<LinkedList<MapNode>> temp = search.tripPlannerDistance("Harmonie", 150);
		for(int i = 0; i < temp.size(); i++) {
			System.out.println(temp.get(i));
			System.out.println(search.getFinalDist(temp.get(i)));
		}
	}
}
