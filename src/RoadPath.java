
public class RoadPath {
	private String roadName;
	private int MPH;
	private int length;
	
	public RoadPath(String roadName, int MPH, int length) {
		this.roadName = roadName;
		this.MPH = MPH;
		this.length = length;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getTime() {
		return length / MPH;
	}
	
	public String getRoadName() {
		return roadName;
	}
	
}
