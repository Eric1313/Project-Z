package connection;

import java.io.*;
import java.net.*;

/**
 * Client object that connects to a server of Project Z.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see Server
 * @since 1.0
 * @version 1.0
 */
public class Client implements Runnable {
	private Server server;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	
	private boolean connected;
	
	/**
	 * Client constructor.
	 * 
	 * @param socket
	 *            the socket that the client is on.
	 * @param server
	 *            the server that the client is on.
	 */
	public Client(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		// Set up the client's input and output
		try {
			this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Error setting up input");
			e.printStackTrace();
		}
		
		try {
			this.output = new PrintWriter(this.socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Error setting up output");
			e.printStackTrace();
		}
		
		while (this.connected) {
			String message = this.readLine();
		}
	}
	
	private void disconnect() {
		this.connected = false;
		this.server.disconnect(this);
	}
	
	private String readLine() {
		String message = "";
		
		try {
			message = this.input.readLine();
		} catch (IOException e) {
			this.disconnect();
		}
		
		if (message == null) {
			this.disconnect();
		}
		
		return message;
	}
}