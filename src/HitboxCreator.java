import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class HitboxCreator extends JFrame{

	private BufferedImage image;
	private String hlp = "Help";
	private String abs = "Absolute";
	private String rel = "Relative/Ratio";
	private String gen = "Generate Example Java Class";
	private String opt = "Options";
	private String tab = abs;
	private Data data = new Data();
	private Polygon poly = new Polygon2(new int[]{}, new int[]{}, 0);
	private String extensions[] = {".jpg", ".png", ".gif", ".jpeg", ".bmp", ".wbmp"};
	private int pointHeld = -1;
	private Pane pane;
	private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	private int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	public HitboxCreator() {
		
		this.setTitle("HitboxCreator (Cameron O'Neil)");
		this.setSize(900, 700);
		this.setMinimumSize(new Dimension(385, 500));
		//this.setResizable(false);
		
		try{
			getImageFromURL(new URL("http://washhumane.typepad.com/.a/6a00e54eed855d8834017ee9cf65f4970d-pi"));
		} catch (IOException e) {
			try {
				getImageFromFile();
			} catch (IOException e1) {
				System.exit(0);
				e1.printStackTrace();
			}
		}
		
		class MyMenuBar extends JMenuBar{
			
			public MyMenuBar() {
				JMenu open = new JMenu("Open");
					JMenuItem file = new JMenuItem("File");
					file.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								getImageFromFile();
								reset();
								pane.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
								HitboxCreator.this.pack();
								HitboxCreator.this.setLocationRelativeTo(null);
								repaint();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});
					open.add(file);
					JMenuItem link = new JMenuItem("Link");
					link.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								getImageFromURL();
								reset();
								pane.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
								HitboxCreator.this.pack();
								HitboxCreator.this.setLocationRelativeTo(null);
								repaint();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});
					open.add(link);
				this.add(open);
				JMenu tools = new JMenu("Tools");
					JMenuItem reset = new JMenuItem("Reset");
					reset.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							reset();
						}
					});
					tools.add(reset);
				this.add(tools);
			}
		}
		
		setJMenuBar(new MyMenuBar());
		
		pane = new Pane();
		
		JPanel paneHolder = new JPanel();
		paneHolder.setBorder(new EmptyBorder(5, 5, 5, 5));
		paneHolder.add(pane, BorderLayout.CENTER);
		this.getContentPane().add(paneHolder);
		this.getContentPane().add(data, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void reset(){
		poly = new Polygon2(new int[]{}, new int[]{}, 0);
		updateData();
		repaint();
	}
	
	class Pane extends JPanel{
		
		public Pane(){
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1){
						for (int i =0; i <poly.npoints; i++)
							if (new Rectangle(poly.xpoints[i]-5, poly.ypoints[i]-5, 10,10).contains(e.getPoint()))
								pointHeld = i;
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
			g.drawRect(0, 0, image.getWidth()-1, image.getHeight()-1);
			
			g.setColor(new Color(color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, .7f));
			for (int i =0; i <poly.npoints; i++)
				g.fillRect(poly.xpoints[i] -5, poly.ypoints[i] -5, 10, 10);
			
			g.setColor(new Color(color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f, .5f));
			g.fill(poly);
		}
	}
	
	private void getImageFromFile() throws IOException{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {return null;}
			
			@Override
			public boolean accept(File f) {
				
				if (f.isDirectory()) return true;
				
				for (String extension : extensions)
					if (f.getName().toLowerCase().endsWith(extension))
						return true;
				return false;
			}
		});
		
		File file = null;
		
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		if (file != null) image = ImageIO.read(file);
		else throw new IOException("File null");
		
		autoScaleImage();
	}
	
	private void getImageFromURL() throws IOException{
		getImageFromURL(new URL(JOptionPane.showInputDialog("InsertLink:", "http://washhumane.typepad.com/.a/6a00e54eed855d8834017ee9cf65f4970d-pi")));
	}

	private void getImageFromURL(URL url) throws IOException{
		image = ImageIO.read(url);
		autoScaleImage();
	}
	
	private void autoScaleImage(){
		
		int horizontalBorder = 280;
		int verticalBorder = 10;
		float zoomScale = .01f;
		zoom = 1.0f;
		
		if (image.getWidth() * (zoom + zoomScale) < screenWidth - verticalBorder && image.getHeight() * (zoom + zoomScale) < screenHeight - horizontalBorder){
			while (image.getWidth() * (zoom + zoomScale) < screenWidth - verticalBorder && image.getHeight() * (zoom + zoomScale) < screenHeight - horizontalBorder){
				zoom += zoomScale;
			}
		}
		else if (image.getWidth() * (zoom) > screenWidth - verticalBorder || image.getHeight() * (zoom) > screenHeight - horizontalBorder){
			
			System.out.println("Image must be less than: " + (screenWidth - verticalBorder) + " " + (screenHeight - horizontalBorder));
			
			while (image.getWidth() * (zoom) > screenWidth - verticalBorder || image.getHeight() * (zoom) > screenHeight - horizontalBorder){
				zoom -= zoomScale;
				System.out.println("Zoom: " + (zoom));
				System.out.println(image.getWidth() * (zoom) + " " + image.getHeight() * (zoom));
			}
		}
		else{
			return;
		}
		
		try{
			image = toBufferedImage(image.getScaledInstance(Math.round(image.getWidth()*(zoom)), Math.round(image.getHeight()*(zoom)), BufferedImage.SCALE_SMOOTH));
			pane.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			repaint();
			pack();
			setLocationRelativeTo(null);
		}catch(Exception e){};
		
		JOptionPane.showMessageDialog(null, "Image was autoscaled to " + Math.round(zoom*100) + "% for a better fit on your monitor\nCoordinates for the hitbox will be scaled accordingly");
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	float zoom = 1.0f;
	
	private void updateData() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (tab.equals(abs)){
					data.absolute.xxx.setText(Arrays.toString(scaleToZoom(poly.xpoints)));
					data.absolute.yyy.setText(Arrays.toString(scaleToZoom(poly.ypoints)));
					data.absolute.ppp.setText(Arrays.deepToString(toPoints(scaleToZoom(poly.xpoints), scaleToZoom(poly.ypoints))));
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
	
	Color color = Color.red;
	
	class Data extends JTabbedPane{
		
		private AbsoluteAndRelative absolute = new AbsoluteAndRelative();
		private AbsoluteAndRelative relative = new AbsoluteAndRelative();
		private Generate generate = new Generate();
		private Options options = new Options();
		private Help help = new Help();
		
		public Data() {
			
			add(abs, absolute);
			add(rel, relative);
			add(gen, generate);
			add(opt, options);
			add(hlp , help);
			
			setPreferredSize(new Dimension(0, 145));
			
			addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					tab = ((JTabbedPane) changeEvent.getSource()).getTitleAt(((JTabbedPane) changeEvent.getSource()).getSelectedIndex());
					updateData();
				}
			});
		}
		
		class Options extends JPanel{
			
			public Options() {

				JButton colorButton = new JButton ("Change Color");
				colorButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						color = JColorChooser.showDialog(HitboxCreator.this, "Chose a color:", color);
						HitboxCreator.this.repaint();
					}
				});
				
				this.add(colorButton);
			}
			
		}
		
		class Help extends JPanel{
			
			public Help(){
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
				JLabel lblRightMouse = new JLabel("Right Mouse: Add Hitbox Point (Min of 3)");
				lblRightMouse.setToolTipText(lblRightMouse.getText());
				lblRightMouse.setBorder(new EmptyBorder(5, 8, 0, 0));
				add(lblRightMouse);
				
				JLabel lblLeftMouse = new JLabel("Left Mouse: Move Selected Hitbox Point");
				lblLeftMouse.setToolTipText(lblLeftMouse.getText());
				lblLeftMouse.setBorder(new EmptyBorder(5, 8, 0, 0));
				add(lblLeftMouse);
				
				JLabel lblAbsolute = new JLabel("Absolute Positioning: Faster, Only use if object stays the same width and height");
				lblAbsolute.setToolTipText(lblAbsolute.getText());
				lblAbsolute.setBorder(new EmptyBorder(5, 8, 0, 0));
				add(lblAbsolute);
				
				JLabel lblRelative = new JLabel("Relative Positioning: Slower, will adjust with the object's width and height");
				lblRelative.setToolTipText(lblRelative.getText());
				lblRelative.setBorder(new EmptyBorder(5, 8, 0, 0));
				add(lblRelative);
				
				JLabel note = new JLabel("Note: Do NOT crop the image later. However, If you are using relative you may stretch or shrink it");
				note.setToolTipText(note.getText());
				note.setBorder(new EmptyBorder(5, 8, 0, 0));
				add(note);
				
				JLabel note2 = new JLabel("Note: With Relative you do not have to maintain aspect ratio");
				note2.setToolTipText(note2.getText());
				note2.setBorder(new EmptyBorder(5, 8, 0, 0));
				add(note2);
			}
			
		}
		
		class AbsoluteAndRelative extends JPanel{
			
			private JTextField xxx, yyy, ppp;

			public AbsoluteAndRelative() {
				
				//X
				JPanel x = new JPanel();
				JLabel xx = new JLabel("X: ");
				xxx = new JTextField("[]");
				JButton xCopy = new JButton("Copy");
				xCopy.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						StringSelection selection = new StringSelection(xxx.getText());
						clipboard.setContents(selection, selection);
					}
				});
				x.setLayout(new BorderLayout());
				x.add(xx, BorderLayout.WEST);
				x.add(xxx);
				x.add(xCopy, BorderLayout.EAST);
				
				//Y
				JPanel y = new JPanel();
				JLabel yy = new JLabel("Y: ");
				yyy = new JTextField("[]");
				JButton yCopy = new JButton("Copy");
				yCopy.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						StringSelection selection = new StringSelection(yyy.getText());
						clipboard.setContents(selection, selection);
					}
				});
				y.setLayout(new BorderLayout());
				y.add(yy, BorderLayout.WEST);
				y.add(yyy);
				y.add(yCopy, BorderLayout.EAST);
				
				//Points
				JPanel p = new JPanel();
				JLabel pp = new JLabel("Points: ");
				ppp = new JTextField("[]");
				JButton pCopy = new JButton("Copy");
				pCopy.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						StringSelection selection = new StringSelection(ppp.getText());
						clipboard.setContents(selection, selection);
					}
				});
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
				
				this.setLayout(new BorderLayout());
				
				JPanel holder = new JPanel();
				
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
									String xs = Arrays.toString(scaleToZoom(poly.xpoints));
									next = next.replace("/*<Insert xCoords Here>*/", xs.substring(1, xs.length()-1));
								}
								else if (next.contains("/*<Insert yCoords Here>*/")){
									String ys = Arrays.toString(scaleToZoom(poly.ypoints));
									next = next.replace("/*<Insert yCoords Here>*/", ys.substring(1, ys.length()-1));
								}
								else{
									
								}
								
								output += next + System.getProperty("line.separator");
								
							}
							
							//System.out.println(output);
							
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
									next = next.replace("{<Insert yRatios Here>}", toFloatArrayString(getRelativeY(poly.ypoints)));
								}
								else{
									
								}
								
								output += next + System.getProperty("line.separator");
								
							}
							
							//System.out.println(output);
							
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
				
				holder.add(absolute);
				holder.add(relative);
				this.add(holder, BorderLayout.CENTER);
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
	
	public int[] scaleToZoom(int[] points){
		int[] temp = points.clone();
		for (int i=0; i < temp.length; i++)
			temp[i] = Math.round(temp[i] / zoom);
		return temp;
	}

	public void say(Object o){
		System.out.println(o);
	}
	
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){};
		
		new HitboxCreator();
	}
}
