package Model.minim;

public class Minim {
	private static boolean				DEBUG				= false;
	
	/** @invisible
	 * 
	 * Used internally to report error messages. These error messages will
	 * appear in the console area of the PDE if you are running a sketch from
	 * the PDE, otherwise they will appear in the Java Console.
	 * 
	 * @param message
	 *            the error message to report
	 */
	public static void error(String message)
	{
		System.out.println( "=== Minim Error ===" );
		System.out.println( "=== " + message );
		System.out.println();
	}

	/** @invisible
	 * 
	 * Displays a debug message, but only if {@link #debugOn()} has been called.
	 * The message will be displayed in the console area of the PDE, if you are
	 * running your sketch from the PDE. Otherwise, it will be displayed in the
	 * Java Console.
	 * 
	 * @param message
	 *            the message to display
	 * @see #debugOn()
	 */
	public static void debug(String message)
	{
		if ( DEBUG )
		{
			String[] lines = message.split( "\n" );
			System.out.println( "=== Minim Debug ===" );
			for ( int i = 0; i < lines.length; i++ )
			{
				System.out.println( "=== " + lines[i] );
			}
			System.out.println();
		}
	}

	/**
	 * Turns on debug messages.
	 */
	public static void debugOn()
	{
		DEBUG = true;
	}


	/**
	 * Turns off debug messages.
	 * 
	 */
	public static void debugOff()
	{
		DEBUG = false;

	}
	
}
