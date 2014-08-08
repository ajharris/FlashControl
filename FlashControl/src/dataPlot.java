import java.util.Hashtable;

import processing.core.PApplet;



public class dataPlot extends PApplet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int wl, value;
	
	private Hashtable<Integer, Integer> values = new Hashtable<Integer, Integer>();
	
	private Spectrometer spec = new Spectrometer();
	
	public void setup(){

		frameRate(800);

		size(1100, 220);
		background(0);

		value = 100;
		spec.initialize();
		spec.close();
	}
	
	public void draw(){
//		specDraw(lc.getVal()[0], lc.getVal()[1]);

		wl = 375;
		if(spec.toString().contains("Spectrometer")){
			values = spec.getVal();
//			StdOut.println("Finished loading array");
		
			for(int j = 0; j < values.size(); j++){
				StdOut.println("loop");
				
				wl += 2;
				if(values.get(wl) != null){
					specDraw(wl, values.get(wl));
				}

			}
		}
	}
	
	public void specDraw(int fr, int val){
		stroke(0);
		line(fr-200, height, fr-200, 0);		
		stroke(255);
		line(fr-200, height, fr-200, height-val);

	}

	public void setSpec(Spectrometer spec2) {
		spec = spec2;
		
	}

}
