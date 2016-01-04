package server;

import client.Client;

public class Game implements Runnable {

	@Override
	public void run() {
	}

	public static void main(String[] args) {
		new Thread(new Server(5000)).start();
		new Client("127.0.0.1", 5000);
	}
}
