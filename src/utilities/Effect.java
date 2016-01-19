package utilities;

public class Effect extends Sound implements Runnable {

	public Effect(String name) {
		super(name, false);
	}

	public void run() {
		super.play();
	}

	public void play() {
		(new Thread(this)).start();
	}

}