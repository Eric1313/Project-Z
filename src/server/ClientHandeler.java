package server;

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
public class ClientHandeler implements Runnable {
	private Server server;
	private Socket socket;
	private  DataInputStream input;
	private  DataOutputStream output;
	private short messageType;
	
	private int playerNo;
	private String name;
	private boolean connected;
	
	/**
	 * Client constructor.
	 * 
	 * @param socket
	 *            the socket that the client is on.
	 * @param server
	 *            the server that the client is on.
	 */
	public ClientHandeler(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		// Set up the client's input and output
		try {
			this.input = new DataInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			System.err.println("Error setting up input");
			e.printStackTrace();
		}
		
		try {
			this.output = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Error setting up output");
			e.printStackTrace();
		}
		
		try
		{
			this.name = input.readUTF();
			output.writeByte(playerNo);
			output.writeByte(this.server.getNoOfClients());
					
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		while (true)
		{
			try
			{
				messageType=input.readShort();
				switch(messageType)
				{
				case 1:
					break;
				case 2:
					break;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}
		
//		String msg = this.server.getNoOfClients() + " " + this.getPlayerNo() + " ";
//		
//		for (int player = 0; player < this.server.getNoOfClients(); player++) {
//			msg += this.server.getName(player) + " | ";
//		}
//		
//		this.sendMessage(msg);
//		
//		while (this.connected && this.server.isRunning()) {
//			msg = this.readLine();
//			
//			char msgType = msg.charAt(0);
//			
//			switch (msgType) {
//			case 'm':
//				break;
//			case 's':
//				break;
//			}
//		}
	}
//	
//	private void disconnect() {
//		this.connected = false;
//		this.server.disconnect(this);
//	}
//	
//	private String readLine() {
//		String message = "";
//		
//		try {
//			message = this.input.readLine();
//		} catch (IOException e) {
//			this.server.disconnect(this);
//			this.disconnect();
//		}
//		
//		if (message == null) {
//			this.server.disconnect(this);
//			this.disconnect();
//		}
//		
//		return message;
//	}
//	
//	public void sendMessage(String msg) {
//		this.output.println(msg);
//		this.output.flush();
//	}
//
	public int getPlayerNo() {
		return this.playerNo;
	}

	public void setPlayerNo(int playerNo) {
		this.playerNo = playerNo;
	}
	
	public String getName() {
		return this.name;
	}
}