package dragondungeon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.TooManyListenersException;

import dragondungeon.test.comPortTest;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class comPortJoystick implements DDInput, SerialPortEventListener {

	SerialPort serialPort;
	Thread readThread;
	
	BufferedReader reader;
    InputStream inputStream;
	
	float speedX, speedY;
	boolean knapp;
	
	Q queue;

	public comPortJoystick() {
		CommPortIdentifier portId = null;

		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

				//System.out.println(portId.getName());

				if (portId.getName().equals("/dev/ttyACM0")) {
					break;
				}
			}
		}

		try {
			serialPort = (SerialPort) portId.open("DragonDungeon", 2000);
		} catch (PortInUseException e) {
			System.out.println(e);
		}
		
		try {
			//reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			
			inputStream = serialPort.getInputStream();
			
			
		} catch (IOException e) {
			System.out.println(e);
		}
		
		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			System.out.println(e);
		}
		
		serialPort.notifyOnDataAvailable(true);
		try {
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			System.out.println(e);
		}

	}

	public void serialEvent(SerialPortEvent event) {
		System.out.println(event.getEventType());
		switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
        	
        	byte[] readBuffer = new byte[20];
        	
        	
			try {
				//StringTokenizer st = new StringTokenizer(reader.readLine(), ";");
				
				String linje = "";
				char inn = 0;
				while(inn != 13 && inn != '\n') {
	                   inn = (char) inputStream.read();
	                   linje += inn;
                }
				
				System.out.println(linje);
				
				StringTokenizer st = new StringTokenizer(linje, ";");
				
				speedX = Float.parseFloat(st.nextToken());
				speedX -= 507;
				speedX /= -10;
				
				speedY = Float.parseFloat(st.nextToken());
				speedY -= 524;
				speedY /= 10;
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
           
           
           break;
        }
    }


	public void kalibrer() {

	}

	public float getSpeedX() {
		System.out.println("SpeedX = " + speedX);
		return speedX;
	}

	public float getSpeedY() {
		System.out.println("SpeedY = " +speedY);
		return speedY;
	}

}
