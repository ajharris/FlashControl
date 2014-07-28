import java.util.List;

import com.sun.jna.*;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;


public class Spectrometer {

	
	public interface SpectControl extends Library{
		SpectControl INSTANCE = (SpectControl)Native.loadLibrary("as161", SpectControl.class);
		public int AVS_Init(int port);
		public int AVS_Measure(int iTime, int aScans, PointerByReference spectrum);
		public int AVS_Done();
		public int AVS_GetScopeData(char c, DoubleByReference spectrum);
		public int AVS_MeasureByInterval(int iTime, int aScans, int nDuration, PointerByReference hWnd);
		public void AVS_GetNumChannels(IntByReference num);
		public void AVS_GetLambda(char c, DoubleByReference num);
	}
	
	static public void main(String args[]){
		SpectControl spec = (SpectControl) Native.loadLibrary("as161", SpectControl.class);
		if(spec.AVS_Init(-1) == 0){
			PointerByReference spectrum = null;
			DoubleByReference num = new DoubleByReference();
			if(spec.AVS_Measure(2,100, spectrum) == 0){
				spec.AVS_GetLambda('0', num);
				StdOut.print(num.getValue());
				spec.AVS_GetScopeData('0', num);
				StdOut.println(num.getValue());
			}
			
//			StdOut.println(num.getValue());
			if(spec.AVS_Done() == 0) StdOut.println("Spec closed");
		}
		else StdOut.println("No spec found");
		
	}
}


