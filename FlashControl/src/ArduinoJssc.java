import jssc.*;

public class ArduinoJssc {
	public static void main(){
		String[] portNames = SerialPortList.getPortNames();
		
		for(int i = 0; i<portNames.length; i++){
			StdOut.println(portNames[i]);
			SerialPort serialPort = new SerialPort(portNames[i]);
			try {
				serialPort.openPort();
				serialPort.setParams(	SerialPort.BAUDRATE_9600,
										SerialPort.DATABITS_8,
										SerialPort.STOPBITS_1,
										SerialPort.PARITY_NONE);
				byte[] o = {(byte)1, (byte)2, (byte)100};
				serialPort.writeBytes(o);
				byte[] buffer = serialPort.readBytes(10);
				serialPort.closePort();
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
		
	}
}
