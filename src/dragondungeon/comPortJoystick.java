package dragondungeon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
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
	
	LinkedList<Integer> data;

	public comPortJoystick() {
		
		data = new LinkedList<Integer>();
		
		
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
        	
        	
        	
			try {
				
				int dataMengde = inputStream.available();
				
				while(dataMengde > 0) {
					data.add(inputStream.read());
				}
				
				spisData();
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
           
           
           break;
        }
    }


	private void spisData() {
		System.out.println("NamNam");
		
		ListIterator<Integer> iterator = data.listIterator(data.size());
		
		//finn riktig posisjon
		boolean startOfline = false;
		boolean endOfLine = false;
		while(iterator.hasPrevious()) {
			Integer tegn = iterator.previous();
			
			if(tegn == '\r' && endOfLine == false) {
				endOfLine = true;
			} else if(tegn == '\n' && endOfLine == true) {
				startOfline = true;
				break;
			}
		}
		
		//ukomplett datsett
		if(!startOfline) return;
		
		//les komplett datalinje
		int[] verdier = new int[4];
		
		int teller = 0;
		while(iterator.hasNext()) {
			
			Integer tegn = iterator.next();
			
			if(tegn == ';') {
				teller++;
				continue;
			} else if(tegn == '\r') {
				break;
			}
			
			verdier[teller] =  ( (verdier[teller] << 3) + (verdier[teller] << 1) + (tegn - 48) );
			
		}
		
		//slett hode / eldste data
		while(iterator.hasPrevious()) {
			data.removeFirst();
		}
		
		speedX = ( verdier[0] -506 ) / 25;
		speedY = ( verdier[1] -524 ) / 25;
		
		knapp = verdier[2] == 1;
		
		
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
