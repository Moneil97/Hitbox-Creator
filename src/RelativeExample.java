import java.awt.Polygon;
import java.util.Arrays;


public class RelativeExample {

	private int x,y,width,height;
	private Polygon poly = new Polygon();
	
	public RelativeExample(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		size = xRatios.length;
		generatePoints();
	}
	
	//Should be a rectangle
	float[] xRatios = {.2f, .8f, .8f, .2f};
	float[] yRatios = {.3f, .3f, .7f, .7f};
	int size;

	@SuppressWarnings("serial")
	private void generatePoints() {
		
		int xs[] = new int[size];
		int ys[] = new int[size];
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + xRatios[i] * width);
			ys[i] = Math.round(y + yRatios[i] * height);
		}
		
		System.out.println(poly = new Polygon(xs, ys, size){

			@Override
			public String toString() {
				String output = "\nAbsolute X: " + Arrays.toString(xpoints);
				output += "\nAbsolute Y: " + Arrays.toString(ypoints);
				return output;
			}
			
		});
	}
	
	public Polygon getPoly(){
		return poly;
	}

	public static void main(String[] args) {
		new RelativeExample(50, 40, 20, 30);
		new RelativeExample(50, 40, 200, 300);
	}

}
