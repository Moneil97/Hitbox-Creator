import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class HitboxCreator extends JFrame{

	private File file;
	private BufferedImage image;
	//private List<Point> points;
	public Polygon poly = new Polygon(){
		
		@Override
		public String toString() {
			String output = "\nAbsolute X: " + Arrays.toString(xpoints);
			output += "\nAbsolute Y: " + Arrays.toString(ypoints);
			output += "\nRelative X: " + Arrays.toString(getRelativeX(xpoints));
			output += "\nRelative Y: " + Arrays.toString(getRelativeY(ypoints)) + "\nPoints: ";
			
			for (int i =0; i < xpoints.length; i++)
				output += "(" + xpoints[i] + "," + ypoints[i] + "), ";
			
			return output;
		}
		
		private double[] getRelativeX(int[] xpoints) {
			
			double rel[] = new double[xpoints.length];
			
			for (int i =0; i < xpoints.length; i++)
				rel[i] = xpoints[i] / (double) image.getWidth();
			
			return rel;
		}
		
		private double[] getRelativeY(int[] ypoints) {

			double rel[] = new double[ypoints.length];
			
			for (int i =0; i < ypoints.length; i++)
				rel[i] = ypoints[i] / (double) image.getHeight();
			
			return rel;
		}
	};

	public HitboxCreator() {
		
		this.setTitle("HitboxCreator (Cameron O'Neil)");
		this.setSize(900, 700);
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		else{
			System.exit(0);
		}
		
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//points = new ArrayList<Point>();
		
		class Pane extends JPanel{
			
			public Pane(){
				addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						//points.add(e.getPoint());
						poly.addPoint(e.getX(), e.getY());
						updateData();
						repaint();
					}
				});
			}

			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				
				g.drawImage(image, 0, 0, null);
				
				g.setColor(Color.red);
				
				for (int i =0; i <poly.npoints; i++)
					g.fillRect(poly.xpoints[i] -5, poly.ypoints[i] -5, 10, 10);

				//System.out.println(poly);
				g.fill(poly);
			}
		}
		
		
		
		this.getContentPane().add(new Pane());
		this.getContentPane().add(data, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	Data data = new Data();
	
	protected void updateData() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				data.ab.xxx.setText(Arrays.toString(poly.xpoints));
				data.ab.yyy.setText(Arrays.toString(poly.ypoints));
			}
		});
	}

	public static void main(String[] args) {
		new HitboxCreator();
	}
	
//	class Polygon2 extends Polygon{
//		
//		
//		
//	}
	
	class Data extends JTabbedPane{
		
		public Absolute ab = new Absolute();
		
		public Data() {
			
			add("Absolute", ab);
			add("Relative/Ratio", new Relative());
			add("Generate Java HitBox Method", new Generate());
		}
		
		class Absolute extends JPanel{
			
			public JTextField xxx;
			private JTextField yyy;
			private JTextField ppp;

			public Absolute() {
				
				//X
				JPanel x = new JPanel();
				JLabel xx = new JLabel("X: ");
				xxx = new JTextField("[]");
				JButton xCopy = new JButton("Copy");
				x.setLayout(new BorderLayout());
				x.add(xx, BorderLayout.WEST);
				x.add(xxx);
				x.add(xCopy, BorderLayout.EAST);
				
				//Y
				JPanel y = new JPanel();
				JLabel yy = new JLabel("Y: ");
				yyy = new JTextField("[]");
				JButton yCopy = new JButton("Copy");
				y.setLayout(new BorderLayout());
				y.add(yy, BorderLayout.WEST);
				y.add(yyy);
				y.add(yCopy, BorderLayout.EAST);
				
				//Points
				JPanel p = new JPanel();
				JLabel pp = new JLabel("Points: ");
				ppp = new JTextField("[]");
				JButton pCopy = new JButton("Copy");
				p.setLayout(new BorderLayout());
				p.add(pp, BorderLayout.WEST);
				p.add(ppp);
				p.add(pCopy, BorderLayout.EAST);
				
				
				//Add to Panel
				setLayout(new GridLayout(3,1));
				add(x);
				add(y);
				add(p);
				
			}
			
//			private String toPoint(){
//				String output = "";
//				for (int i =0; i < xpoints.length; i++)
//					output += "(" + xpoints[i] + "," + ypoints[i] + "), ";
//			}
			
		}
		
		class Relative extends JPanel{
			
		}
		
		class Generate extends JPanel{
			
		}
		
	}

}
