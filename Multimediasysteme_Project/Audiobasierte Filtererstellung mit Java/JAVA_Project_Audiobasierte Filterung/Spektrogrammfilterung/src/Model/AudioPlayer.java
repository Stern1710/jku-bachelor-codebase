package Model;

/**
 * Class starts and ends the PlayThread
 * @author Christopher Holzweber
*/
public class AudioPlayer {
	Thread thread;
	PlayThread pt;
	
	public AudioPlayer(String originalFilePath) {
		pt = new PlayThread(originalFilePath);
		thread = new Thread(pt);
	}
	
	/**
	 * starts the new thread
	 */
	public void startplay() {
		thread.start();
	}
	
	/**
	 * stops playing the music file
	 */
	public void stopplay() {
		pt.stopFlag();
	}

	/**
	 * tells the caller, if the playing thread is still alive
	 * @return Boolean if thread is alive; if thread is not instantiated, returns always false
	 */
	public boolean threadIsAlive() {
		return thread != null ? thread.isAlive() : false;
	}

	public PlayThread getPlayThread() {
		return pt;
	}
}
