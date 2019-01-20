package com.SpaceWars.SocketComm;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Set;

import com.SpaceWars.GameData.GameData;
import com.SpaceWars.pojo.Player;

class ClientHandler extends Thread {
	final ObjectOutputStream out;
	final ObjectInputStream in;
	final Socket s;
	String clientNum;

	// Constructor
	public ClientHandler(Socket s, String clientNum, ObjectInputStream ois, ObjectOutputStream oos) {
		this.s = s;
		this.out = oos;
		this.in = ois;
		this.clientNum = clientNum;
	}

	@Override
	public void run() {

		Player received;

		while (true) {
			try {
				// receive the answer from client
				received = (Player) in.readObject();

				clientNum = received.getName();
				String actionTaken = received.getAction();
				GameData.addNewPlayer(received);

				out.reset();
				out.writeObject(GameData.getAllConnectedPlayers());
				out.flush();

				if (actionTaken.equals("EXIT") || GameData.isCloseAllClients()) {
					System.out.println(">>> Client " + s + " sends exit...");
					System.out.println(">>> Closing this connection.");

					Map<String, Player> allPlayers = GameData.getAllConnectedPlayers().getAllPlayers();

					Set<String> playerKeys = allPlayers.keySet();

					for (String playerName : playerKeys) {
						if (playerName.equals(clientNum)) {
							GameData.removePlayer(allPlayers.get(playerName));
							System.out.println(">>> player:" + playerName + " removed from the game");
							break;
						}
					}

					// closing resources
					out.close();
					in.close();
					s.close();
					System.out.println(">>> Connection closed");
					break;
				}

				// creating Date object
			} catch (SocketException | EOFException se) {
				try {
					GameData.removePlayer(GameData.getAllConnectedPlayers().getAllPlayers().get(clientNum));
					out.close();
					in.close();
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(">>> Connection closed from client:" + clientNum);
				System.out.println(">>> player:" + clientNum + " removed from the game");
				break;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				break;
			}
		}

		return;
	}
}
