import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class RelativeExampleTester extends JFrame{

	public RelativeExampleTester() {
		
		class Mid extends JPanel implements Runnable{
			
			private RelativeExample hi;
			private RelativeExample hi2;
			RelativeExample[] his = new RelativeExample[2];

			public Mid() {
				hi = new RelativeExample(0, 0, 100, 100, 4, 6);
				hi2 = new RelativeExample(0, 0, 500, 500, 2, 2);
				his[0] = hi;
				his[1] = hi2;
				new Thread(this).start();
			}
			
			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				
				Graphics2D g = (Graphics2D) g1;
				
				g.setColor(Color.blue);
				g.drawRect(hi2.getX(),hi2.getY(),hi2.getWidth(),hi2.getHeight());
				g.fill(hi2.getPoly());
				
				g.setColor(Color.red);
				g.drawRect(hi.getX(),hi.getY(),hi.getWidth(),hi.getHeight());
				g.fill(hi.getPoly());
			}

			@Override
			public void run(){
				
				while (true){
					
					for (RelativeExample hi : his){
						hi.translate(hi.xSpeed, hi.ySpeed);
						
						if (hi.getX() <= 0)
							hi.xSpeed = Math.abs(hi.xSpeed);
						if (hi.getX() + hi.getHeight() >= this.getWidth())
							hi.xSpeed = -Math.abs(hi.xSpeed);
						if (hi.getY() <= 0)
							hi.ySpeed = Math.abs(hi.ySpeed);
						if (hi.getY() + hi.getHeight() >= this.getHeight())
							hi.ySpeed = -Math.abs(hi.ySpeed);
					}
					
					repaint();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
		
		this.add(new Mid());
		this.setSize(900, 700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new RelativeExampleTester();
	}

}
