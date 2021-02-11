package contacts;

import java.util.Random;

/**
 * Class containing constants for doing the experiments. 
 */
public class Constants {
	
	/** Seed value for the random number generator. 
	 * Use seed for deterministic configuration
	 */
	private static final int SEED = 6;   
	
	/** The random number generator used to generate the entity locations*/
	public static final Random RAND = new Random(SEED); 
	
	/** The distance value when entities are getting into contact */
	public static final int CONTACT_DISTANCE = 10;

	/** Size of single area for splitting */
	public static final int NUM_OF_SPLITS = 64;

	/** Threshhold for recursive actions */
	public static final int TH_SIZE = 64;
	public static final int TH_ENTITIES = 128;

	// TEST SETTINGS
	/** The size of the area. An area with SIZE * SIZE is created. */
//	public static final int SIZE =    4096;

	/** The number of entities for the area.  */
//	public static final int N_ENTITIES = 1000;

	// SMALL SETTINGS
//	public static final int SIZE =    524_288;
//	public static final int N_ENTITIES = 100_000;

	// MODERATE SETTINGS
//	public static final int SIZE =      1_048_576;
//	public static final int N_ENTITIES = 500_000;

	// LARGE SETTINGS
	public static final int SIZE =    8_388_608;
	public static final int N_ENTITIES = 10_000_000;
	
}
