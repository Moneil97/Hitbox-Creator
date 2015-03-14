import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class HitboxCreator extends JFrame{

	private File file;
	private BufferedImage image;
	private List<Point> points;

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
		
		points = new ArrayList<Point>();
		
		class Pane extends JPanel{
			
			public Pane(){
				addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						points.add(e.getPoint());
						repaint();
					}
				});
			}
			
			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				
				g.drawImage(image, 0, 0, null);
				
				for (Point p : points)
					g.fillRect(p.x, p.y, 2, 2);
				
				g.setColor(Color.red);
				
				Polygon p = new Polygon(getXs(), getYs(), getXs().length){
					
					@Override
					public String toString() {
						String output = "\nX: " + Arrays.toString(xpoints);
						output += "\nY: " + Arrays.toString(ypoints) + "\nPoints: ";
						
						for (int i =0; i < xpoints.length; i++)
							output += "(" + xpoints[i] + "," + ypoints[i] + "), ";
						
						return output;
					}
					
				};
				System.out.println(p);
				g.fill(p);
			}

			private int[] getXs() {
				int[] x = new int[points.size()];
				
				for (int i =0; i < points.size(); i++)
					x[i] = points.get(i).x;
				return x;
			}
			
			private int[] getYs() {
				int[] y = new int[points.size()];
				
				for (int i =0; i < points.size(); i++)
					y[i] = points.get(i).y;
				return y;
			}
		}
		
		this.getContentPane().add(new Pane());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new HitboxCreator();
	}

}
