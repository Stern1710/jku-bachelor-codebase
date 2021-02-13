package TrafficControlDetect;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import common.TrafficControlDetect.StreetData;
import common.TrafficControlDetect.StreetName;
import org.json.JSONObject;
/**
 * This can be seen as the dection server, which recieves sensor and camera data
 * @author Christopher Holzweber
 *
 */
public  class DetectionNetwork {
	
	/**
	 * Flag for checking server output!
	 */
	private final boolean showOutput = false;
	//maps which store up to date detected value
	private ConcurrentMap<StreetName, ConcurrentMap<Integer,Integer>> cityUnmotorized;
	private ConcurrentMap<StreetName, ConcurrentMap<Integer,Integer>> cityMotorized;
	public static final long SERVER_TIMEOUT = 5000;
	/**
	 * Defines the used Port for the server connection
	 */
	public  int PORT = 7777;
	/**
	 * Terminate Flag is used to close sever process
	 */
	private boolean terminate = false;

	/**
	 * Each new client gets it's own ID
	 */
	private int ID = 1;

	/**
	 * ServerSocketChannel is used for connection with the client
	 */
	private ServerSocketChannel serverSocket;



	// Storing the active entites in this list
	public DetectionNetwork() {
		cityUnmotorized = new ConcurrentHashMap<>();
		cityMotorized = new ConcurrentHashMap<>();
	}

	//Add new street to the server
	public void addStreet(StreetName name) {
		cityUnmotorized.put(name, new ConcurrentHashMap<>());
		cityMotorized.put(name, new ConcurrentHashMap<>());
	}
	void start() {

		try {
			// prepare selector, which will react to different action
			Selector selector = Selector.open();
			// create server connection
			serverSocket = ServerSocketChannel.open();
			serverSocket.bind(new InetSocketAddress("localhost", PORT));
			// put server in non blocking mode
			serverSocket.configureBlocking(false);
			// reigster the accept event in the selector
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);
			// bytebuffer used for reading position string and writing
			// contact objects
			ByteBuffer buffer = ByteBuffer.allocate(256);
			// charsets used for stringchatting
			CharsetDecoder dec = StandardCharsets.UTF_8.newDecoder();
			CharsetEncoder enc = StandardCharsets.UTF_8.newEncoder();
			while (!terminate) {
				try {
					// select events of this particular moment
					selector.select(SERVER_TIMEOUT);
					// list used for iterating over pending events
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> iter = selectedKeys.iterator();
					while (iter.hasNext() && !terminate) {

						// get event key
						SelectionKey key = iter.next();
						// get entity, which is attached to the event, so we know,
						// which client is sending its position
						Integer attachment = (Integer) key.attachment(); // get attached entity

						// check if a new client wants to connect to the server
						if (key.isAcceptable()) {
							// encoder used for charbuffering(sending strings to the client)
							if(showOutput) {
								System.out.println("Client with ID=" + ID + " joined the detecting! on PORT "+PORT);
							}
							// accept new client
							SocketChannel client = serverSocket.accept();
							// non blocking mode
							client.configureBlocking(false);
							// register key to the selector, so we can read position data
							SelectionKey k = client.register(selector, SelectionKey.OP_READ);

							// create new ClientInformation with given ID and attach it to the client source
							//k.attach(new ClientInformation(client, ID)); // attach entity
							k.attach(ID);
							// send ID to the client using charbuffer encoding
							String s = ID + "";
							client.write(enc.encode(CharBuffer.wrap(s)));
							ID++;
						}

						// event was a position data of a already registered client
						if (key.isReadable()) {
							// prepare for reading data as string from channel
							buffer.clear();
							SocketChannel client = (SocketChannel) key.channel();
							int inputbytes = client.read(buffer); // store number read bytes
							if (inputbytes < 0) {// client has closed if this happens
								client.close();// get rid of the client
								//setEntityOffline(attachment.getEntity()); // get entity out of area
								System.out.println("Detector ID=" + attachment + " now offline");
								break;
							}
							// prepare buffer for getting coorindates
							buffer.flip();
							// client sent a new position data in form of string, decode it
							String jstr = dec.decode(buffer).toString();
							//now perform json actions
							if(jstr.contains("Sensor")) {
								JSONObject jObject = new JSONObject(jstr);
							       
								StreetName street = StreetName.valueOf(jObject.getString("StreetName"));

							       int id = Integer.parseInt(jObject.getString("ID"));

							       int part= Integer.parseInt(jObject.getString("detectedPart"));

							    cityMotorized.get(street).put(id, part);

							}else if(jstr.contains("Camera")){ //camera type
								JSONObject jObject = new JSONObject(jstr);
							       
								StreetName street = StreetName.valueOf(jObject.getString("StreetName"));

							       int id = Integer.parseInt(jObject.getString("ID"));

							       int partMotor= Integer.parseInt(jObject.getString("motorized"));

							       int partUnmotor= Integer.parseInt(jObject.getString("unmotorized"));

							       cityMotorized.get(street).put(id, partMotor);
							       cityUnmotorized.get(street).put(id, partUnmotor);
							}
							if(showOutput) {
								System.out.println("Server "+PORT+" got data from " +jstr);
							}
							
						}
						iter.remove();
					}

				} catch (Exception e) {
					e.printStackTrace();
					// if an error occurs during channels conversation
					terminate(); // end connection because data conversion failed, error with clients
				}
			}
			System.out.println("ENDED SERVER on PORT "+PORT);
		} catch (Exception e) {
			terminate();
			e.printStackTrace();
		} finally {
			try {

				serverSocket.close();
				System.out.println("Closed Server on PORT "+PORT);
			} catch (IOException ex) {
				System.out.println("Error closing the Server socket");
			}
		}
	}

	public StreetData getCurrentStreetData(StreetName name) {
		int motor = 0;
		int unmotor = 0;
		for(Integer s : cityUnmotorized.get(name).values()) {
			unmotor += s;
		}
		for(Integer s : cityMotorized.get(name).values()) {
			motor += s;
		}
		int total = motor + unmotor;
		return new StreetData(total, motor, unmotor, null);
	}
	
	void terminate() {
		terminate = true;
	}
}
