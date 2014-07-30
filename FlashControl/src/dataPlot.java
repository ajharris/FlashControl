import processing.core.PApplet;



public class dataPlot extends PApplet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int freq, value;
	private ArduinoController lc = new ArduinoController();
	
	public void setup(){

		frameRate(800);

		size(1100, 220);
		background(0);
		freq = 475;
		value = 100;
		lc.initialize();
	}
	
	public void draw(){
		specDraw(lc.getVal()[0], lc.getVal()[1]);
		println(frameRate);
	}
	
	public void specDraw(int fr, int val){
		stroke(0);
		line(fr-100, height, fr-100, 0);		
		stroke(255);
		line(fr-100, height, fr-100, height-val);

	}

}
