

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LEDArray implements Serializable{
	private int LEDnm[];
	private int arrayPower[];
	private int frequency[] = new int [] {8500, 8500, 8500, 8500, 8500, 8500, 8500, 8500, 8500, 8500, 8500, 8500};
	
	public LEDArray(int numOfElements){
		arrayPower = new int[numOfElements];
		for(int i = 0; i<numOfElements; i++) arrayPower[i] = 0;
		int LEDnm [] = new int[]{425,455,470,525,595,625,660,730,850,940,1020};
	}
	
	public void setElement(int e, int v){
		arrayPower[e] = v;
	}
	
	public int getElement(int i){
		return arrayPower[i];
	}
	public int getWavelength(int i){
		return LEDnm[i];
	}

	public int length() {
		return arrayPower.length;
	}

	public void savePreset(String filename)throws IOException {
        try {
          FileOutputStream file = new FileOutputStream(filename);
          ObjectOutputStream out = new ObjectOutputStream(file);
          
          out.writeObject(this);
          
          out.close();
        } catch ( IOException e ) {
           e.printStackTrace();
        }
		
	}
	
	public LEDArray loadPreset() throws ClassNotFoundException, IOException{
		
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Preset Files", "pre");
	    chooser.setFileFilter(filter);
	    Component parent = null;
		int returnVal = chooser.showOpenDialog(parent);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	
			try {
				FileInputStream fileIn = new FileInputStream(chooser.getSelectedFile());
				ObjectInputStream in = new ObjectInputStream(fileIn);
				return (LEDArray) in.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	    }
		return null;
	}

	public int getFreq(int i) {

		return frequency[i];
	}

	public void setFreq(String pinNo, String freq) {
		frequency[Integer.parseInt(pinNo)] = Integer.parseInt(freq);
	}
}
