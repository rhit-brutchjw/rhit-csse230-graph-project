import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JComponent;

public class MapComponent extends JComponent {
	public Loader load;
	HashMap<String, MapNode> map;
	LinkedList<MapNode> path;
	ArrayList<LinkedList<MapNode>> goal;

	public MapComponent(Loader load) {
		super();
		this.load = load;
		load.populateData("StateParkList");
		map = load.createLocations();

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		doDrawing(g2);
		if (goal == null && path != null) {
			doLine(g2, path);
		}else if(path == null && goal != null) {
			for (int i = 0; i< goal.size(); i++) {
				doLine(g2, goal.get(i));
			}
		}
	}

	private void doDrawing(Graphics g) {
		g.setColor(new Color(255, 69, 0));
		for (String key : map.keySet()) {

			g.fillOval(map.get(key).getx() + 31, -1 * map.get(key).gety() - 11, 15, 15);

		}

		g.setFont(new Font("Verdana", Font.BOLD, 15));
		g.setColor(new Color(0, 100, 0));
		for (String key : map.keySet()) {
			if (key.equals("Turkey Run")) {
				g.drawString(map.get(key).name, map.get(key).getx() + 50, -1 * map.get(key).gety() + 9);
			} else {

				g.drawString(map.get(key).name, map.get(key).getx() + 50, -1 * map.get(key).gety() + 2);
			}

		}

	}

	public void doLine(Graphics2D g, LinkedList<MapNode> result) {
		g.setStroke(new BasicStroke(3));
		g.setColor(new Color(255, 69, 0));
		for (int i = 0; i < result.size() - 1; i++) {
			MapNode m1 = result.get(i);
			MapNode m2 = result.get(i + 1);
			g.drawLine(m1.getx() + 37, -1 * m1.gety() - 5, m2.getx() + 37, -1 * m2.gety() - 5);

		}
	}

	public void setResult(LinkedList<MapNode> result) {
		this.path = result;

	}
	

	public void setGoal(ArrayList<LinkedList<MapNode>> topThree) {
		this.goal = topThree;
		
	}

}