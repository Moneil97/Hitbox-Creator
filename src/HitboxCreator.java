import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class HitboxCreator extends JFrame{

	private File file;
	private BufferedImage image;
	private String abs = "Absolute";
	private String rel = "Relative/Ratio";
	private String gen = "Generate Java HitBox Class";
	private String tab = abs;
	private Data data = new Data();
	private Polygon poly = new Polygon2(new int[]{}, new int[]{}, 0);

	private int pointHeld = -1;

	public HitboxCreator() {
		
		this.setTitle("HitboxCreator (Cameron O'Neil)");
		this.setSize(900, 700);
		
		try{
			image = ImageIO.read(getClass().getResourceAsStream("testImage.png"));
		} catch (IOException e) {
			e.printStackTrace();
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
			} catch (IOException e1) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		class Pane extends JPanel{
			
			public Pane(){
				addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1){
							
							for (int i =0; i <poly.npoints; i++){
								if (new Rectangle(poly.xpoints[i]-5, poly.ypoints[i]-5, 10,10).contains(e.getPoint())){
									say(i);
									pointHeld = i;
								}
							}
							
						}
						else if (e.getButton() == MouseEvent.BUTTON3){
							poly.addPoint(e.getX(), e.getY());
							updateData();
							repaint();
						}
					}
					
					@Override
					public void mouseReleased(MouseEvent e) {
						pointHeld = -1;
					}
				});
				
				addMouseMotionListener(new MouseAdapter(){
					@Override
					public void mouseDragged(MouseEvent e) {
						if (pointHeld >= 0){
							say("drag " + pointHeld);
							
							int[] xs = poly.xpoints;
							int[] ys = poly.ypoints;
							
							xs[pointHeld] = e.getX();
							ys[pointHeld] = e.getY();
							
							poly = new Polygon2(xs, ys, xs.length);
							updateData();
							repaint();
						}
					}
				});
				
				this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			}

			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				Graphics2D g = (Graphics2D) g1;
				
				g.drawImage(image, 0, 0, null);
				g.setColor(Color.black);
				g.drawRect(0,0,image.getWidth(), image.getHeight());
				
				g.setColor(Color.red);
				
				for (int i =0; i <poly.npoints; i++)
					g.fillRect(poly.xpoints[i] -5, poly.ypoints[i] -5, 10, 10);
				
				g.fill(poly);
			}
		}
		
		this.getContentPane().add(new Pane());
		this.getContentPane().add(data, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	protected void updateData() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (tab.equals(abs)){
					data.absolute.xxx.setText(Arrays.toString(poly.xpoints));
					data.absolute.yyy.setText(Arrays.toString(poly.ypoints));
					data.absolute.ppp.setText(Arrays.deepToString(toPoints(poly.xpoints, poly.ypoints)));
				}
				
				else if (tab.equals(rel)){
					float relx[] = getRelativeX(poly.xpoints);
					float rely[] = getRelativeY(poly.ypoints);
					data.relative.xxx.setText(toFloatArrayString(relx));
					data.relative.yyy.setText(toFloatArrayString(rely));
					data.relative.ppp.setText(Arrays.deepToString(toPoints(relx, rely)));
				}
			}
			
			private int[][] toPoints(int[] xPoints, int[] yPoints){
				int points[][] = new int[xPoints.length][];
				
				for (int i=0; i < xPoints.length; i++)
					points[i] = new int[] {xPoints[i], yPoints[i]};
					
				return points;
			}
			
			private float[][] toPoints(float[] xPoints, float[] yPoints){
				float points[][] = new float[xPoints.length][];
				
				for (int i=0; i < xPoints.length; i++)
					points[i] = new float[] {xPoints[i], yPoints[i]};
					
				return points;
			}
		});
	}

	public static void main(String[] args) {
		new HitboxCreator();
	}
	
	class Data extends JTabbedPane{
		
		private AbsoluteAndRelative absolute = new AbsoluteAndRelative();
		private AbsoluteAndRelative relative = new AbsoluteAndRelative();
		private Generate generate = new Generate();
		
		public Data() {
			
			add(abs, absolute);
			add(rel, relative);
			add(gen, generate);
			add("Help", null);
			
			setPreferredSize(new Dimension(0, 130));
			
			addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					tab = ((JTabbedPane) changeEvent.getSource()).getTitleAt(((JTabbedPane) changeEvent.getSource()).getSelectedIndex());
					updateData();
				}
			});
		}
		
		class AbsoluteAndRelative extends JPanel{
			
			private JTextField xxx, yyy, ppp;

			public AbsoluteAndRelative() {
				
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
		}
		
		class Generate extends JPanel{
			
			public Generate() {
				JButton absolute = new JButton("Generate Java Class for Absolute Positioning");
				absolute.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							Scanner scan = new Scanner(getClass().getResourceAsStream("AbsoluteClassTemplate.txt"));
							String output = "";
							while (scan.hasNextLine()){
								
								String next = scan.nextLine();
								
								if (next.contains("/*<Insert xCoords Here>*/")){
									String xs = Arrays.toString(poly.xpoints);
									next = next.replace("/*<Insert xCoords Here>*/", xs.substring(1, xs.length()-1));
								}
								else if (next.contains("/*<Insert yCoords Here>*/")){
									String ys = Arrays.toString(poly.ypoints);
									next = next.replace("/*<Insert yCoords Here>*/", ys.substring(1, ys.length()-1));
								}
								else{
									
								}
								
								output += next + System.getProperty("line.separator");
								
							}
							
							System.out.println(output);
							
							File f = new File("AbsoluteClassTemplate.java");
							
							try {
								f.createNewFile();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
							
							FileOutputStream out = new FileOutputStream(f);
							try {
								out.write(output.getBytes());
								out.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							ProcessBuilder pb = new ProcessBuilder("Notepad.exe", f.getPath());
							try {
								pb.start();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							scan.close();
							
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				});
				JButton relative = new JButton("Generate Java Class for Relative Positioning");
				relative.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							
							Scanner scan = new Scanner(getClass().getResourceAsStream("RelativeClassTemplate.txt"));
							String output = "";
							while (scan.hasNextLine()){
								
								String next = scan.nextLine();
								
								if (next.contains("{<Insert xRatios Here>}")){
									next = next.replace("{<Insert xRatios Here>}", toFloatArrayString(getRelativeX(poly.xpoints)));
								}
								else if (next.contains("{<Insert yRatios Here>}")){
									next = next.replace("{<Insert yRatios Here>}", toFloatArrayString(getRelativeX(poly.ypoints)));
								}
								else{
									
								}
								
								output += next + System.getProperty("line.separator");
								
							}
							
							System.out.println(output);
							
							byte[] text = output.getBytes();
							File f = new File("RelativeClassTemplate.java");
							
							try {
								f.createNewFile();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
							
							FileOutputStream out = new FileOutputStream(f);
							try {
								out.write(text);
								out.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							ProcessBuilder pb = new ProcessBuilder("Notepad.exe", f.getPath());
							try {
								pb.start();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							
							scan.close();
							
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				});
				
				add(absolute);
				add(relative);
			}
			
		}
		
	}
	
	private float[] getRelativeX(int[] xpoints) {
		
		float rel[] = new float[xpoints.length];
		
		for (int i =0; i < xpoints.length; i++)
			rel[i] = xpoints[i] / (float) image.getWidth();
		
		return rel;
	}
	
	private float[] getRelativeY(int[] ypoints) {

		float rel[] = new float[ypoints.length];
		
		for (int i =0; i < ypoints.length; i++)
			rel[i] = ypoints[i] / (float) image.getHeight();
		
		return rel;
	}
	
	private String toFloatArrayString(float[] array){
		
		if (array.length == 0) return "{}";
		
		String output = "{ ";
		
		for (float f: array){
			output += f + "f, ";
		}
		
		return output.substring(0, output.length()-2) + "}";
	}
	
	class Polygon2 extends Polygon{
		
		public Polygon2(int[] xs, int[] ys, int size) {
			super(xs, ys, size);
		}

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
		
		public void addPoint(int x, int y) {
			if (npoints + 1 > xpoints.length) {
				int[] newx = new int[npoints + 1];
				System.arraycopy(xpoints, 0, newx, 0, npoints);
				xpoints = newx;
			}
			if (npoints + 1 > ypoints.length) {
				int[] newy = new int[npoints + 1];
				System.arraycopy(ypoints, 0, newy, 0, npoints);
				ypoints = newy;
			}
			xpoints[npoints] = x;
			ypoints[npoints] = y;
			npoints++;
			if (bounds != null) {
				if (npoints == 1) {
					bounds.x = x;
					bounds.y = y;
				} else {
					if (x < bounds.x) {
						bounds.width += bounds.x - x;
						bounds.x = x;
					} else if (x > bounds.x + bounds.width)
						bounds.width = x - bounds.x;
					if (y < bounds.y) {
						bounds.height += bounds.y - y;
						bounds.y = y;
					} else if (y > bounds.y + bounds.height)
						bounds.height = y - bounds.y;
				}
			}
		}
		
	}

	public void say(Object o){
		System.out.println(o);
	}
}
