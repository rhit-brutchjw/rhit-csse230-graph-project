import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Loader {
	// Lists of XML info
	List<Double> ewPos = new ArrayList<Double>();
	List<Double> nsPos = new ArrayList<Double>();
	ArrayList<String> names = new ArrayList<String>();
	// Indiana boundaries
	Double leftBound = -88.3;
	Double rightBound = -84.6;
	Double topBound = 42.;
	Double botBound = 37.6;
	// Map size values
	int hor;
	int vert;

	public Loader(int horWidth, int vertWidth) {
		// Loader needs the horizontal and vertical sizes of the
		// on screen map
		this.hor = horWidth;
		this.vert = vertWidth;
	}

	public int long2x(double ew) {
		// Converts longitude to x pos
		double rightBase = ew - leftBound;
		return (int) Math.floor(rightBase / (rightBound - leftBound) * hor);
	}

	public int lat2y(double ns) {
		// Converts Latitude to y pos
		double topBase = ns - topBound;
		return (int) Math.floor(-topBase / (botBound - topBound) * vert);
	}

	public HashMap<String, MapNode> createLocations() {
		// Generates a hashmap of MapNodes has each locations name as a key.
		HashMap<String, MapNode> locations = new HashMap<String, MapNode>();
		for (int k = 0; k < names.size(); ++k) {
			locations.put(names.get(k),
					new MapNode(long2x(ewPos.get(k)), lat2y(nsPos.get(k)), names.get(k), ewPos.get(k), nsPos.get(k)));
		}
		return locations;
	}

	// XML Loading Code
	public void populateData(String fileName) {
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder bob = fact.newDocumentBuilder();
			Document XMLTable = bob.parse(new File(fileName + ".xml"));
			XMLTable.getDocumentElement().normalize();
			NodeList nList = XMLTable.getElementsByTagName("Name");
			NodeList latList = XMLTable.getElementsByTagName("Latitude");
			NodeList longList = XMLTable.getElementsByTagName("Longitude");
			// actually creating arraylists
			for (int k = 0; k < nList.getLength(); ++k) {
				this.names.add(nList.item(k).getTextContent());
				this.ewPos.add(Double.parseDouble(longList.item(k).getTextContent()));
				this.nsPos.add(Double.parseDouble(latList.item(k).getTextContent()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void autoGenNeighbours(Double radius, HashMap<String, MapNode> map) {
		Double rad2 = Math.pow(radius, 2);
		for (int k = names.size() - 1; k >= 0; --k) {
			for (int j = names.size() - 1; j >= 0; --j) {
				double xCheck = Math.pow(ewPos.get(j) - ewPos.get(k), 2);
				double yCheck = Math.pow(nsPos.get(j) - nsPos.get(k), 2);
				if (rad2 > xCheck + yCheck && j != k) {
					map.get(names.get(k)).addNeighbour(map.get(names.get(j)));
					map.get(names.get(j)).addNeighbour(map.get(names.get(k)));

				}
			}
		}
		generateRoadPaths(map);
	}
	
	public void generateRoadPaths(HashMap<String, MapNode> map) {
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder bob = fact.newDocumentBuilder();
			Document XMLTable = bob.parse(new File("RoadData.xml"));
			XMLTable.getDocumentElement().normalize();
			NodeList toList = XMLTable.getElementsByTagName("to");
			NodeList fromList = XMLTable.getElementsByTagName("from");
			NodeList MPHList = XMLTable.getElementsByTagName("MPH");
			NodeList lengthList = XMLTable.getElementsByTagName("length");
			for(int i = 0; i < toList.getLength(); i++) {
				double MPH = Double.parseDouble(MPHList.item(i).getTextContent());
				int length = Integer.parseInt(lengthList.item(i).getTextContent());
				MapNode to = map.get(toList.item(i).getTextContent());
				MapNode from = map.get(fromList.item(i).getTextContent());
				to.neighborToRoad.put(from, new RoadPath(to, from, MPH, length));
				from.neighborToRoad.put(to, new RoadPath(from, to, MPH, length));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}