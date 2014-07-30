import processing.core.PApplet;


public class dataPlot extends PApplet{
	
	private int freq, count;
	private ArduinoController data = new ArduinoController();
	
	public void setFreq(int fq){
		freq = fq;
	}
	public void setCount(int ct){
		count = ct;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setup(){
		size(1000,200);
		background(0);
		data.initialize();
	}
	
	public void draw(){
		putSpectrum(data.getVal()[0], data.getVal()[1]);
//		background(0);

	}
	public void putSpectrum(int f, int c){
		if (f == 375) background(0);
		stroke(255);
		line(f-200,height, f-200, height-c);
	}

}
