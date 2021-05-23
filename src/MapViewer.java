/* Map Viewer Class
 * Author Dixon Ramey
 * Purpose: To display the map and nodes
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	public JLabel dest1 = new JLabel("Start: ");
	public JLabel dest2 = new JLabel("Finish: ");
	public JButton findR = new JButton();
	public JButton reset = new JButton();
	public JButton zoomin = new JButton();
	public JButton zoomout = new JButton();
	public JLabel dist = new JLabel("Distance: ");
	public JLabel time = new JLabel("Time: ");
	public JTextField gTime = new JTextField("Enter Goal Time Here");
	
	public JTextField gDist = new JTextField("Enter Goal Distance Here");
	public JButton goalFind = new JButton("Find goal Route");
	
	
	
	private ArrayList<String> locs = new ArrayList<String>();

	Loader Load = new Loader(550, 865);
	private MapComponent mapc = new MapComponent(Load);

	public JComboBox<String> firstDestination = new JComboBox<String>();
	public JComboBox<String> secondDestination = new JComboBox<String>();
	public JComboBox<String> goalStart = new JComboBox<String>();
	public JFrame mainGUI = new JFrame();
	public JPanel bckpanel = new JPanel();
	public JPanel optionsPanel = new JPanel();
	public JPanel tripPanel = new JPanel();
	public JLabel goalPick = new JLabel("<---- Pick your start!");

	BufferedImage bufI;

	private String[] selectionOptions = { "Distance", "Time" };
	public JComboBox<String> selection = new JComboBox<>(selectionOptions);
	public JComboBox<String> goalOp = new JComboBox<>(selectionOptions);

	HashMap<String, MapNode> map;

	public MapViewer() {
//	
		try {
			bufI = ImageIO.read(new File("IndianaMap.png"));

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
		map = Load.createLocations();
		Load.autoGenNeighbours(1.0, map);

		for (String key : map.keySet()) {
			firstDestination.addItem(key);
			secondDestination.addItem(key);
			goalStart.addItem(key);
			locs.add(key);

//			System.out.println("x: " + map.get(key).getx() + "y: " + map.get(key).gety());
		}
		

		Dimension mp = new Dimension(900, 800);
		Dimension op = new Dimension(480, 10);
		Dimension tfop = new Dimension(480,10);
		
		tripPanel.setPreferredSize(tfop);
		optionsPanel.setPreferredSize(op);
		mapPanel.setPreferredSize(mp);
		
		optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 700, 0));
		optionsPanel.setLayout(new GridLayout(0, 2, 20, 20));

		tripPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 850, 0));
		tripPanel.setLayout(new GridLayout(0,2,20,20));
		
		mapPanel.setLayout(new BorderLayout());
		mapPanel.add(mapc);

		findR.setText("Find Route");
		reset.setText("Reset");

		optionsPanel.add(findR);
		optionsPanel.add(reset);
		optionsPanel.add(dest1);
		optionsPanel.add(firstDestination);
		optionsPanel.add(dest2);
		optionsPanel.add(secondDestination);
		
		

		optionsPanel.add(dist);
		optionsPanel.add(time);

		findR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dist.setText("Distance: ");
				time.setText("Time: ");
				mapc.setResult(null);
				mainGUI.repaint();

				String Start = firstDestination.getSelectedItem().toString();
				String End = secondDestination.getSelectedItem().toString();
				AStarSearch search = new AStarSearch(map);
				MapNode st = map.get(Start);
				MapNode go = map.get(End);

				LinkedList<MapNode> result;
				dest1.setText("Start: " + Start);
				dest2.setText("End: " + End);
				if (selection.getSelectedItem().toString().equals("Distance")) {
					result = search.search(st, go);
					int miles = search.getFinalDist(result);
					dist.setText("Distance: " + miles + " miles");
				} else if (selection.getSelectedItem().toString().equals("Time")) {
					result = search.searchTime(st, go);
					double hours = search.getFinalTime(result);
					double scale = Math.pow(10, 2);
					hours = Math.round(hours * scale) / scale;
					time.setText("Time: " + hours + " hours");
				} else {
					result = null;
				}
				mapc.setGoal(null);
				mapc.setResult(result);
				mainGUI.repaint();
			}

		});
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dist.setText("Distance: ");
				time.setText("Time: ");
				dest1.setText("Start: ");
				dest2.setText("End: ");
				mapc.setResult(null);
				mapc.setGoal(null);
				mainGUI.repaint();

			}

		});
		
		goalFind.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dist.setText("Distance: ");
				time.setText("Time: ");
				mapc.setResult(null);
				mapc.setGoal(null);
				mainGUI.repaint();
				
				ArrayList<LinkedList<MapNode>> topThree;

				String Start = goalStart.getSelectedItem().toString();
				AStarSearch search = new AStarSearch(map);
				MapNode st = map.get(Start);
				

				try {
					
					if (goalOp.getSelectedItem().toString().equals("Distance")) {
						
						topThree = search.tripPlannerDistance(Start, Integer.parseInt(gDist.getText()));
						
						
					} else if (goalOp.getSelectedItem().toString().equals("Time")) {
						topThree = search.tripPlannerTime(Start, Double.parseDouble(gTime.getText()));
						
					} else {
						topThree = null;
					}
					gTime.setText("Enter Goal Time Here");
					gDist.setText("Enter Goal Miles Here");
					mapc.setResult(null);
					mapc.setGoal(topThree);
					mainGUI.repaint();
					
				}catch(NumberFormatException n) {
					JOptionPane.showMessageDialog(null, "Please enter an integer for your selected cost method");
				}
				
			}
				
			
			
			
			
		});

		mainGUI.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainGUI.setTitle("Map View");

		optionsPanel.add(firstDestination);
		optionsPanel.add(secondDestination);
		optionsPanel.add(selection, BorderLayout.NORTH);
		
		tripPanel.add(gTime);
		tripPanel.add(gDist);
		tripPanel.add(goalFind);
		tripPanel.add(goalOp);
		tripPanel.add(goalStart);
		tripPanel.add(goalPick);
		

		
		mainGUI.add(mapPanel, BorderLayout.WEST);
		mainGUI.add(tripPanel, BorderLayout.CENTER);
		mainGUI.add(optionsPanel, BorderLayout.EAST);

		mainGUI.pack();
		mainGUI.setLocationRelativeTo(null);
		mainGUI.setVisible(true);

	}

	


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
