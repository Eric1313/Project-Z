package utilities;

/**
 * Sound Effect.
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public class SoundEffect extends Sound implements Runnable {

	/**
	 * Creates sound effect object.
	 * 
	 * @param path
	 *            path of file.
	 */
	public SoundEffect(String path) {
		super(path, false);
	}

	/**
	 * Play the sound in a thread.
	 */
	public void run() {
		super.play();
	}

	/**
	 * Play the sound in a thread.
	 */
	public void play() {
		(new Thread(this)).start();
	}
}