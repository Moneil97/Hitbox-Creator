import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Arrays;


public class RelativeExample {

	private int x,y,width,height;
	private Polygon poly = new Polygon();
	
	float[] xRatios = { 0.59016395f, 0.26092896f, 0.25273225f, 0.23360656f,
			0.19945355f, 0.15983607f, 0.117486335f, 0.099726774f, 0.07103825f,
			0.042349726f, 0.013661202f, 0.008196721f, 0.0027322404f,
			0.012295082f, 0.024590164f, 0.040983606f, 0.058743168f,
			0.10519125f, 0.13661203f, 0.19398907f, 0.23497267f, 0.2568306f,
			0.26092896f, 0.6215847f, 0.61885244f, 0.6297814f, 0.65163934f,
			0.6885246f, 0.7363388f, 0.7773224f, 0.8306011f, 0.86885244f,
			0.9030055f, 0.94262296f, 0.9644809f, 0.97540987f, 0.97131145f,
			0.9644809f, 0.9439891f, 0.9112022f, 0.8852459f, 0.8715847f,
			0.88114756f, 0.9030055f, 0.9453552f, 0.965847f, 0.97540987f,
			0.9699454f, 0.94262296f, 0.90710384f, 0.8606557f, 0.8101093f,
			0.7527322f, 0.70628417f, 0.6748634f, 0.6338798f, 0.61202186f,
			0.5969945f, 0.5942623f };
	float[] yRatios = { 0.29124236f, 0.29124236f, 0.26476577f, 0.24236253f,
			0.24439919f, 0.2545825f, 0.2729124f, 0.29327902f, 0.3156823f,
			0.3503055f, 0.39714867f, 0.42769858f, 0.4725051f, 0.5132383f,
			0.57026476f, 0.6191446f, 0.6537678f, 0.6822811f, 0.7046843f,
			0.7189409f, 0.7148676f, 0.700611f, 0.67617106f, 0.67617106f,
			0.73319757f, 0.7820774f, 0.84521383f, 0.898167f, 0.9307536f,
			0.9307536f, 0.92057025f, 0.9144603f, 0.892057f, 0.8411405f,
			0.7861507f, 0.7169043f, 0.6639511f, 0.6191446f, 0.5967413f,
			0.5743381f, 0.53360486f, 0.4765784f, 0.4215886f, 0.39307535f,
			0.36863545f, 0.3197556f, 0.25661916f, 0.18940938f, 0.124236256f,
			0.07942974f, 0.042769857f, 0.034623217f, 0.04684318f, 0.05498982f,
			0.06924643f, 0.105906315f, 0.14663951f, 0.19551934f, 0.21995927f };

	int size;
	public int xSpeed, ySpeed;
	
	public RelativeExample(int x, int y, int width, int height) {
		this(x,y,width,height, 0,0);
	}
	
	public RelativeExample(int x, int y, int width, int height, int xSpeed, int ySpeed) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		
		size = xRatios.length;
		generatePoints();
	}
	
	@SuppressWarnings("serial")
	private void generatePoints() {
		
		int xs[] = new int[size];
		int ys[] = new int[size];
		
		for (int i =0; i  < size; i++){
			xs[i] = Math.round(x + xRatios[i] * width);
			ys[i] = Math.round(y + yRatios[i] * height);
		}
		
		poly = new Polygon(xs, ys, size){

			@Override
			public String toString() {
				String output = "\nAbsolute X: " + Arrays.toString(xpoints);
				output += "\nAbsolute Y: " + Arrays.toString(ypoints);
				return output;
			}
			
		};
		
		System.out.println(poly);
	}
	
	public void setWidth(int width){
		setDimmensions(width, height);
	}
	
	public void setHeight(int height){
		setDimmensions(width, height);
	}
	
	public void setDimmensions(Dimension d){
		setDimmensions(d.width, d.height);
	}
	
	public void setDimmensions(int width, int height){
		this.width = width;
		this.height = height;
		generatePoints();
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getSize() {
		return size;
	}
	
	public Polygon getPoly(){
		return poly;
	}

	public static void main(String[] args) {
		new RelativeExample(50, 40, 20, 30);
		new RelativeExample(50, 40, 200, 300);
	}

	public void setX(int x) {
		setPoint(x,y);
	}
	
	public void setY(int y) {
		setPoint(x,y);
	}
	
	public void setPoint(Point p){
		setPoint(p.x, p.y);
	}
	
	public void setPoint(int x, int y){
		poly.translate(x - this.x, y - this.y);
		this.x = x;
		this.y = y;
	}
	
	public void translate(int x, int y){
		poly.translate(x, y);
		this.x = this.x + x;
		this.y = this.y + y;
	}

}
