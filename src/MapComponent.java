import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import javax.swing.JComponent;

public class MapComponent extends JComponent {
	public Loader load;
	HashMap<String, MapNode> map;

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

	public void doLine(Graphics g, String loc1, String loc2) {

	}

}