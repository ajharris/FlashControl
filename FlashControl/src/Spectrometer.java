

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener;

import java.util.Enumeration; 
import java.util.Hashtable;

import cc.arduino.*;

public class Spectrometer implements SerialPortEventListener{
	Hashtable<Integer, Integer> values = new Hashtable<Integer, Integer>();
	SerialPort serialPort;
    /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
		"usbmodem",// Mac OS X
        "ttyACM0", // Raspberry Pi
		"ttyUSB0", // Linux
//		"COM3", "COM1", 
		"COM" // Windows
	};
/**
* A BufferedReader which will be fed by a InputStreamReader 
* converting the bytes into characters 
* making the displayed results codepage independent
*/
private BufferedReader input;
/** The output stream to the port */
private OutputStream output;
/** Milliseconds to block while waiting for port open */
private static final int TIME_OUT = 2000;
/** Default bits per second for COM port. */
private static final int DATA_RATE = 9600;
public Hashtable<Integer, Integer> getVal(){
	return values;
}

public void initialize(String portName) {
    // the next line is for Raspberry Pi and 
    // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
    // System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

CommPortIdentifier portId = null;
Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

//First, Find an instance of serial port as set in PORT_NAMES.
while (portEnum.hasMoreElements()) {
CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
for (String portVal : PORT_NAMES) {
	if (currPortId.getName().contains(portName)) {
		portId = currPortId;
		StdOut.println(portId.getName());
		break;
	}
}
}
if (portId == null) {
System.out.println("Could not find COM port.");
return;
}

try {
// open serial port, and use class name for the appName.
serialPort = (SerialPort) portId.open(this.getClass().getName(),
		TIME_OUT);

// set port parameters
serialPort.setSerialPortParams(DATA_RATE,
		SerialPort.DATABITS_8,
		SerialPort.STOPBITS_1,
		SerialPort.PARITY_NONE);

// open the streams
input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
output = serialPort.getOutputStream();

// add event listeners
serialPort.addEventListener(this);
serialPort.notifyOnDataAvailable(true);
} catch (Exception e) {
System.err.println(e.toString());
}
}

public void initialize() {
            // the next line is for Raspberry Pi and 
            // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
            // System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

	CommPortIdentifier portId = null;
	Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

	//First, Find an instance of serial port as set in PORT_NAMES.
	while (portEnum.hasMoreElements()) {
		CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
		for (String portName : PORT_NAMES) {
			if (currPortId.getName().contains(portName)) {
				portId = currPortId;
				StdOut.println(portId.getName());
				break;
			}
		}
	}
	if (portId == null) {
		System.out.println("Could not find COM port.");
		return;
	}

	try {
		// open serial port, and use class name for the appName.
		serialPort = (SerialPort) portId.open(this.getClass().getName(),
				TIME_OUT);

		// set port parameters
		serialPort.setSerialPortParams(DATA_RATE,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);

		// open the streams
		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
		output = serialPort.getOutputStream();

		// add event listeners
		serialPort.addEventListener(this);
		serialPort.notifyOnDataAvailable(true);
	} catch (Exception e) {
		System.err.println(e.toString());
	}
}

/**
 * This should be called when you stop using the port.
 * This will prevent port locking on platforms like Linux.
 */
public synchronized void close() {
	if (serialPort != null) {
		serialPort.removeEventListener();
		serialPort.close();
	}
}

/**
 * Handle an event on the serial port. Read the data and print it.
 */
public synchronized void serialEvent(final SerialPortEvent oEvent) {
	
	Thread t = new Thread(new Runnable(){
		@Override
		public void run(){
	
			if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				try {
					String inputLine=input.readLine();
					
					String numbers[ ] = new String[2];
					numbers = inputLine.split(" ");
					values.put(Integer.parseInt(numbers[0]),Integer.parseInt(numbers[1]));
					
//					StdOut.println(values[0] + " "+ values[1]);
					Thread.sleep(100);
				} catch (Exception e) {
					System.err.println(e.toString());
				}
			}
		}
	});
	t.start();
	// Ignore all the other eventTypes, but you should consider the other ones.
}

public synchronized void faderMove (final int fader, final int value) throws InterruptedException {
	
	final byte[] array = new byte[]{(byte)1,(byte) fader, (byte) value};
//	System.out.println(array[0] + " "+ array[1]);
	
	Thread t = new Thread(new Runnable(){
		@Override
		public void run(){
		try{
			Thread.sleep(30);
			OutputStream writer = serialPort.getOutputStream();
//			writer.write(fader);
//			writer.write(value);
			writer.write(array);
			Thread.sleep(10);
		}
		catch (Exception e){
			System.out.println(e);
		}
	}});
	t.start();
}


	
	/**
	 * @param args
	 */
public static void main(String[] args) throws Exception {
	ArduinoController main = new ArduinoController();
	main.initialize();
	Thread t=new Thread() {

		public void run() {
			//the following line will keep this app alive for 1000 seconds,
			//waiting for events to occur and responding to them (printing incoming messages to console).
			try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
		}
	};
	t.start();

	System.out.println("Started");
}

	public void setFrequency(String pin, String frequency) {
		int npin = Integer.parseInt(pin);
		int nfrequency = Integer.parseInt(frequency);
		final byte[] array = new byte[]{(byte)2,(byte) npin, (byte) nfrequency};
//		System.out.println(array[0] + " "+ array[1]);
		
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
			try{
				Thread.sleep(30);
				OutputStream writer = serialPort.getOutputStream();
//				writer.write(fader);
//				writer.write(value);
				writer.write(array);
				Thread.sleep(10);
			}
			catch (Exception e){
				System.out.println(e);
			}
		}});
		t.start();
	}

	public void setFrequency(int i, int freq) {
		final byte[] array = new byte[]{(byte)2,(byte) i, (byte) freq};
//		System.out.println(array[0] + " "+ array[1]);
		
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
			try{
				Thread.sleep(30);
				OutputStream writer = serialPort.getOutputStream();
//				writer.write(fader);
//				writer.write(value);
				writer.write(array);
				Thread.sleep(10);
			}
			catch (Exception e){
				System.out.println(e);
			}
		}});
		t.start();
		
	}

		
	}


