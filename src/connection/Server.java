package connection;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Server object that hosts a game of Project Z and handles the players
 * connected to it.
 * 
 * @author Patrick Liu, Eric Chee, Allen Han, Alosha Reymer
 * @see Client
 * @since 1.0
 * @version 1.0
 */
public class Server {
	private ServerSocket socket;
	private ArrayList<Client> clientList;
	private int noOfClients = 0;

	/**
	 * Server constructor.
	 * 
	 * @param port
	 *            the port to set up the server on.
	 */
	public Server(int port) {
		this.clientList = new ArrayList<Client>();

		while (socket == null) {
			try {
				this.socket = new ServerSocket(port);
			} catch (IOException e) {
				System.err.println("Error creating server on port " + port);
				e.printStackTrace();
				port = 5000; // get a new port
			}
		}

		while (true) {
			System.out.println("Waiting for client to connect...");
			try {
				Socket client = this.socket.accept();
				Client newClient = new Client(client, this);
				this.clientList.add(newClient);
				this.noOfClients++;
			} catch (Exception e) {
				System.err.println("Error connecting to client");
				e.printStackTrace();
			}
		}
	}
	
	public void disconnect(Client client) {
		this.clientList.remove(client);
	}
}