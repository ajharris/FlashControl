import processing.core.PApplet;



public class dataPlot extends PApplet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int freq, value;
	public void setup(){
		size(1100, 220);
		background(0);
		freq = 375;
		value = 100;
	}
	
	public void draw(){
		stroke(255);
		line(freq-100, height, freq-100, value);
	}

}
