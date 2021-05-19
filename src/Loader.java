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
	//Lists of XML info
	List<Double> ewPos = new ArrayList<Double>();
	List<Double> nsPos = new ArrayList<Double>();
	ArrayList<String> names = new ArrayList<String>();
	//Indiana boundaries
	Double leftBound = -88.3;
	Double rightBound = -84.6;
	Double topBound = 42.;
	Double botBound = 37.6;
	//Map size values
	int hor;
	int vert;
	public Loader(int horWidth, int vertWidth) {
		//Loader needs the horizontal and vertical sizes of the 
		//on screen map
		this.hor = horWidth;
		this.vert = vertWidth;
	}
	public int long2x(double ew) {
		//Converts longitude to x pos
		double rightBase = ew-leftBound;
		return (int) Math.floor(rightBase/(rightBound-leftBound)*hor);
	}
	public int lat2y(double ns) {
		//Converts Latitude to y pos
		double topBase = ns-topBound;
		return (int) Math.floor(-topBase/(botBound-topBound)*vert);
	}
	public HashMap<String,MapNode> createLocations() {
		//Generates a hashmap of MapNodes has each locations name as a key.
		HashMap<String,MapNode> locations = new HashMap<String,MapNode>();
		for(int k=0;k<names.size();++k) {
			locations.put(names.get(k), new MapNode(long2x(ewPos.get(k)),lat2y(nsPos.get(k))));
		}
		return locations;
	}
	//TODO create roadNode method
	
	//XML Loading Code
	public void populateData(String fileName) {
		try {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder bob = fact.newDocumentBuilder();
		Document XMLTable = bob.parse(new File(fileName + ".xml"));
		XMLTable.getDocumentElement().normalize();
		NodeList nList = XMLTable.getElementsByTagName("Name");
		NodeList latList = XMLTable.getElementsByTagName("Latitude");
		NodeList longList = XMLTable.getElementsByTagName("Longitude");
		//print statements to check if each list has the right data
//		System.out.println(nList.item(0).getTextContent());
//		System.out.println(latList.item(0).getTextContent());
//		System.out.println(longList.item(0).getTextContent());
		//actually creating arraylists
		for(int k = 0; k < nList.getLength(); ++k) {
			this.names.add(nList.item(k).getTextContent());
			this.ewPos.add(Double.parseDouble(longList.item(k).getTextContent()));
			this.nsPos.add(Double.parseDouble(latList.item(k).getTextContent()));
			}
		System.out.println(this.names.toString());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void autoGenNeighbours(Double radius, HashMap<String,MapNode> map) {
		Double rad2 = Math.pow(radius, 2);
		for (int k = names.size()-1; k>0;--k) {
//			System.out.println(names.get(k));
			for (int j = names.size() - k -1 ; j > 0;--j) {
				double xCheck = Math.pow(ewPos.get(j)-ewPos.get(k),2);
				double yCheck = Math.pow(nsPos.get(j)-nsPos.get(k),2);
//				System.out.println(xCheck);
//				System.out.println(yCheck);
				if(rad2 > xCheck + yCheck && j !=k) {
//					System.out.println("Group " + k);
//					System.out.println(names.get(k));
//					System.out.println(names.get(j));
					map.get(names.get(k)).addNeighbour(map.get(names.get(j)));
					map.get(names.get(j)).addNeighbour(map.get(names.get(k)));

				}
			}
		}
	}
	
//TEST CODE FOR THIS CLASS
public static void main(String args[]) {
	Loader Load = new Loader(1000,1000);
	Load.populateData("StateParkList");
	HashMap<String,MapNode> temp =Load.createLocations();
//	System.out.println(temp.get("Falls of the Ohio").toString());
	Load.autoGenNeighbours(1., temp);
//	System.out.println(temp.get("Charlestown").getNeighbors().toString());
	}
}