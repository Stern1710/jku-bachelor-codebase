package TrafficControlDetect;

import common.ControlSystem.SubsystemConnector;
import common.TrafficControlDetect.Street;
import common.TrafficControlDetect.StreetMode;
import common.TrafficControlDetect.StreetName;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.rmi.UnknownHostException;
import java.util.Random;
import java.util.Set;

import Participants.Motorized;

/**
 * This class respresents a Sensor - DetectionType
 * Each street is going to have at least 5 sensors
 * Each sensors works like a client, sending its data to 
 * it corresponding Detection Network.
 * @author Christopher Holzweber, 11803108
 *
 */
public  class Sensor extends DetectionDevice {
	// communication with server
	private ByteBuffer bufferContacts;
	private CharsetEncoder enc = Charset.forName("UTF-8").newEncoder();
	// ID given by the server
	private int cnt = 0;
	private int myId;
	private boolean available = true;
	private int sendingIntervall = 5000;//intervall of sending in millsec
	/**
	 * Defines the used Port for the server connection
	 */
	public static  int PORT = 7777;
	public final StreetName streetID; //sensor rec. on which street it is used
	public final Street street;
	private SubsystemConnector connector;
	public Sensor(Street name, SubsystemConnector connector) {
		this.streetID = name.getStreetID();
		this.street = name;
		this.connector = connector;
	}


	/**
	 * This method starts a new sensor thread, running until terminated.
	 * It is sending data in JSON Format to the DetectionNetwork Server.
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	void start() throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		// Open up Connection to server
		// now we are using SocketChannels for the client
		SocketChannel channel = SocketChannel.open();
		channel.connect(new InetSocketAddress("localhost", PORT));
		
		bufferContacts = ByteBuffer.allocate(256);

		// recieve ID from server
		channel.read(bufferContacts);

		bufferContacts.flip();
		// client sent a new position data in form of string, decode it
		String s = Charset.forName("UTF-8").newDecoder().decode(bufferContacts).toString();
		//String s = new String(bufferContacts.array()).trim(); // trim is used, because buffer is bigger
		myId = Integer.parseInt(s);
		// set method for entity ID

		System.out.println("Sensor Client " + myId + " started and connected to server");

		Thread sendDataThread = new Thread(() -> {

			try {
				while (!Thread.interrupted()) {
					StringBuilder sb = new StringBuilder(); //build up JSON string
					sb.append("{");
					sb.append("\"StreetName\": \"");
					sb.append(streetID.toString());
					sb.append("\",");
					sb.append("\"DetectionType\": \"Sensor\",");
					sb.append("\"ID\": \"");
					sb.append(myId);
					sb.append("\",");
					sb.append("\"available\": \"");
					sb.append(available);
					sb.append("\",");
					int detected;
					if(street.getStreetMode() != StreetMode.BLOCK) {
						detected = 0;
						for(Motorized m : connector.getMotorizedParticipants().values()) {
							if(m.getCurrStreet() == streetID) {
								detected = 1;
								break;
							}
						}
					}else {
						 detected = 0;
					}
					sb.append("\"detectedPart\": \"");
					sb.append(detected);
					sb.append("\"");
					sb.append("}");
					String sender = sb.toString();
					cnt++;
					// writing using Char-buffers instead of a globally defined buffer
					channel.write(enc.encode(CharBuffer.wrap(sender)));
					Thread.sleep(sendingIntervall);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}

		});
		sendDataThread.start();
	}


	@Override
	public DetectionType getType() {
		return DetectionType.SENSOR;
	}
}
