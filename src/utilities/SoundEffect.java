package utilities;

/**
 * Sound Effect
 * 
 * @author Allen Han, Alosha Reymer, Eric Chee, Patrick Liu
 * @since 1.0
 * @version 1.0
 */
public class SoundEffect extends Sound implements Runnable {

	public SoundEffect(String name) {
		super(name, false);
	}

	public void run() {
		super.play();
	}

	public void play() {
		(new Thread(this)).start();
	}

}