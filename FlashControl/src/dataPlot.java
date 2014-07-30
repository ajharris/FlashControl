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
		freq = 475;
		value = 100;
	}
	
	public void draw(){
		specDraw(freq, value);
	}
	
	public void specDraw(int fr, int val){
		stroke(255);
		if(fr == 375) background(0);
		line(fr-100, height, fr-100, val);

	}

}
