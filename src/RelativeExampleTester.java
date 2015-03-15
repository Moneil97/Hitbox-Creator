import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class RelativeExampleTester extends JFrame{

	public RelativeExampleTester() {
		
		class Mid extends JPanel implements Runnable{
			
			private RelativeExample hi, hi2, hi3, hi4;
			private RelativeExample[] his = new RelativeExample[4];

			public Mid() {
				hi = new RelativeExample(0, 0, 100, 60, 4, 6);
				hi2 = new RelativeExample(0, 0, 500, 300, 2, 4);
				hi3 = new RelativeExample(0, 0, 400, 100, 2, 8);
				hi4 = new RelativeExample(0, 0, 100, 300, 8, 3);
				his[0] = hi;
				his[1] = hi2;
				his[2] = hi3;
				his[3] = hi4;
				new Thread(this).start();
			}
			
			@Override
			protected void paintComponent(Graphics g1) {
				super.paintComponent(g1);
				
				Graphics2D g = (Graphics2D) g1;
				
				g.setColor(Color.blue);
				g.drawRect(hi2.getX(),hi2.getY(),hi2.getWidth(),hi2.getHeight());
				g.fill(hi2.getPoly());
				
				g.setColor(Color.green);
				g.drawRect(hi3.getX(),hi3.getY(),hi3.getWidth(),hi3.getHeight());
				g.fill(hi3.getPoly());
				
				g.setColor(Color.orange);
				g.drawRect(hi4.getX(),hi4.getY(),hi4.getWidth(),hi4.getHeight());
				g.fill(hi4.getPoly());
				
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
						if (hi.getX() + hi.getWidth() >= this.getWidth())
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
