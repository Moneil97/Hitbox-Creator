import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Arrays;


public class RelativeExample {

	private int x,y,width,height;
	private Polygon poly = new Polygon();
//	float[] xRatios = {.2f, .8f, .8f, .2f};
//	float[] yRatios = {.3f, .3f, .7f, .7f};
	
	float[] xRatios = { 0.59153f, 0.26092896f, 0.24726775f, 0.19535519f,
			0.10245901f, 0.042349726f, 0.0054644807f, 0.0040983604f,
			0.030054646f, 0.07923497f, 0.17896175f, 0.22950819f, 0.26229507f,
			0.6161202f, 0.6352459f, 0.6939891f, 0.7704918f, 0.8784153f,
			0.9535519f, 0.9726776f, 0.95628417f, 0.87568307f, 0.8784153f,
			0.9508197f, 0.9740437f, 0.96311474f, 0.90983605f, 0.84562844f,
			0.7527322f, 0.6803279f, 0.6270492f, 0.6010929f, 0.59153f };
	float[] yRatios = { 0.2892057f, 0.29327902f, 0.24439919f, 0.24236253f,
			0.28716904f, 0.3503055f, 0.41751528f, 0.50101835f, 0.60488796f,
			0.67209774f, 0.7148676f, 0.72912425f, 0.67617106f, 0.67209774f,
			0.7820774f, 0.892057f, 0.92668027f, 0.9144603f, 0.83299387f,
			0.73319757f, 0.6089613f, 0.5112016f, 0.41344196f, 0.35437882f,
			0.2790224f, 0.16293278f, 0.08553971f, 0.040733196f, 0.036659878f,
			0.073319755f, 0.11201629f, 0.17311609f, 0.27698573f };

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
