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
public class ClientHandler implements Runnable {
	private Server server;
	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
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
	public ClientHandler(Socket socket, Server server) {
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

		this.name = readString();

		while (this.connected && this.server.isRunning()) {
			// first byte is operation code
			switch (readByte()) {
			case 1:// Disconnect
				readByte();
				break;
			case 2:// Move
				readShort();
				break;
			case 3:// Shoot
				readShort();
				readShort();
				// read UID
				break;
			case 4:// Throw
				readShort();
				readShort();
				// readUID
				break;
			case 5:// Melee
				readShort();
				break;
			// read UID
			case 6:// Interact
					// read UID
				break;
			}
		}
	}

	private String readString() {
		String msg = "";

		try {
			msg = this.input.readUTF();
		} catch (IOException e) {
			this.server.disconnect(this);
			// this.disconnect();
		}

		if (msg == null) {
			this.server.disconnect(this);
			// this.disconnect();
		}

		return msg;
	}

	private byte readByte() {
		byte code = 0;

		try {
			code = this.input.readByte();
		} catch (IOException e) {
			this.server.disconnect(this);
			// this.disconnect();
		}

		if (code == 0) {
			this.server.disconnect(this);
			// this.disconnect();
		}

		return code;
	}

	private short readShort() {
		short code = 0;

		try {
			code = this.input.readShort();
		} catch (IOException e) {
			this.server.disconnect(this);
			// this.disconnect();
		}

		if (code == 0) {
			this.server.disconnect(this);
			// this.disconnect();
		}

		return code;
	}

	public void sendMessage(String msg) {
		try {
			this.output.writeUTF(msg);
			this.output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(byte msg) {
		try {
			this.output.writeByte(msg);
			this.output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(short msg) {
		try {
			this.output.writeShort(msg);
			this.output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(int msg) {
		try {
			this.output.writeInt(msg);
			this.output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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