/* Map Viewer Class
 * Author Dixon Ramey
 * Purpose: To display the map and nodes
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;

/**
 * 
 * 
 * @author brutchjw and rameydj and grimbet
 * 
 */
public class MapViewer extends JFrame implements ActionListener {
	public static final int FRAME_WIDTH = 1920;
	public static final int FRAME_HEIGHT = 1080;
	public JButton start = new JButton("Start");

	public JLabel dest1 = new JLabel("Start");
	public JLabel dest2 = new JLabel("Finish");
	public JButton findR = new JButton();
	public JButton reset = new JButton();
	public JButton zoomin = new JButton();
	public JButton zoomout = new JButton();
	public JLabel dist = new JLabel("Distance: ");
	public JLabel time = new JLabel("Time: ");

	private ArrayList<String> locs = new ArrayList<String>();

	Loader Load = new Loader(550, 865);
	private MapComponent mapc = new MapComponent(Load);

	public JComboBox<String> firstDestination = new JComboBox<String>();
	public JComboBox<String> secondDestination = new JComboBox<String>();

	public JFrame mainGUI = new JFrame();
	public JPanel bckpanel = new JPanel();
	public JPanel optionsPanel = new JPanel();

	BufferedImage bufI;

	private String[] selectionOptions = { "Time", "Distance" };
	public JComboBox<String> selection = new JComboBox<>(selectionOptions);

	public MapViewer() {
//	
		try {
			bufI = ImageIO.read(new File("C:\\Users\\rameydj\\Downloads\\indiana-blank.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		JPanel mapPanel = new JPanel() {

			protected void paintComponent(Graphics g) {
				super.paintComponents(g);
				g.drawImage(bufI, 0, 0, null);

			}
		};

		Load.populateData("StateParkList");
		HashMap<String, MapNode> map = Load.createLocations();

		for (String key : map.keySet()) {
			firstDestination.addItem(key);
			secondDestination.addItem(key);
			locs.add(key);

			System.out.println("x: " + map.get(key).getx() + "y: " + map.get(key).gety());
		}

		Dimension mp = new Dimension(1400, 800);
		Dimension op = new Dimension(480, 10);
		optionsPanel.setPreferredSize(op);
		mapPanel.setPreferredSize(mp);

		optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 800, 0));
		optionsPanel.setLayout(new GridLayout(0, 2, 10, 10));

		mapPanel.setLayout(new BorderLayout());
		mapPanel.add(mapc);

		findR.setText("Find Route");
		reset.setText("Reset");
		zoomin.setText("Zoom+");
		zoomout.setText("Zoom-");

		optionsPanel.add(dest1);
		optionsPanel.add(firstDestination);
		optionsPanel.add(dest2);
		optionsPanel.add(secondDestination);
		optionsPanel.add(findR);
		optionsPanel.add(reset);
		optionsPanel.add(zoomin);
		optionsPanel.add(zoomout);
		optionsPanel.add(dist);
		optionsPanel.add(time);

		findR.addActionListener(this);

		mainGUI.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainGUI.setTitle("Map View");

		optionsPanel.add(firstDestination);
		optionsPanel.add(secondDestination);
		optionsPanel.add(selection, BorderLayout.NORTH);

		mainGUI.add(mapPanel, BorderLayout.WEST);

		mainGUI.add(optionsPanel, BorderLayout.EAST);

		mainGUI.pack();
		mainGUI.setLocationRelativeTo(null);
		mainGUI.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int selec1 = firstDestination.getSelectedIndex();
		int selec2 = secondDestination.getSelectedIndex();

		String first = locs.get(selec1);
		String second = locs.get(selec2);
		dist.setText("Distance: " + first);
		time.setText("Time: " + second);

	}

	public static void main(String args[]) {
		new MapViewer();
	}
}