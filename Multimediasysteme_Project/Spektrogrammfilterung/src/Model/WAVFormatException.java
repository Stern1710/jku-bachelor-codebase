package Model;

/**
 * Exception which gets thrown as soon as the WAV File has the wrong Format
 * @author Christopher Holzweber
 *
 */
@SuppressWarnings("serial")
public class WAVFormatException extends Exception {

	public WAVFormatException() {
		super("WAVFormatException");
	}

	public WAVFormatException(String arg0) {
		super(arg0);
	}

}
