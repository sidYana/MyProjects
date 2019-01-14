package com.SpaceWars.SocketComm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import com.SpaceWars.DisplayPageWizard.DisplayWizard;
import com.SpaceWars.DisplayPageWizard.MenuTypes;
import com.SpaceWars.pojo.DataTransferPOJO;
import com.SpaceWars.pojo.Player;

public class Client {

	private Player player;

	private boolean discoveryStatus = false;

	ObjectOutputStream out;
	ObjectInputStream in;

	Socket socket;

	int canShootBulletTimer = 2;
	int count = 0;

	Thread discoveryClientTimerThread;

	private final String DISCOVERY_CLIENT_TIMER_THREAD_NAME = "DISCOVERY_CLIENT_TIMER_THREAD";

	public Client(int screenWidth, int screenHeight, String playerNameGiven, String shipTypeGiven) {
		player = new Player(playerNameGiven, screenWidth, screenHeight, shipTypeGiven);
		if(SocketCommunicationUtil.isLocalServerRunning()) {
			player.setHosting(true);
		}
	}

	private InetAddress startDiscoveryClient() {
		if (discoveryClientTimerThread == null) {
			discoveryClientTimerThread = new Thread(new DiscoveryTimerThread(DiscoveryClient.getInstance()), DISCOVERY_CLIENT_TIMER_THREAD_NAME);
		}
		
		if(!discoveryClientTimerThread.isAlive()) {
			discoveryClientTimerThread.start();
			System.out.println(">>> Discovery Client started");
			return DiscoveryClient.getInstance().startDiscovery();
		}else {
			System.out.println(">>> Discovery Client already started");
			return null;
		}
	}

	public boolean checkForServerInNetwork() {

		InetAddress discoveredAddress = startDiscoveryClient();

		if (discoveredAddress != null) {
			
			System.out.println(">>> Received server address:" + discoveredAddress);
			System.out.println(">>> Establishing client connection with server at address:" + discoveredAddress);
			socket = establishConnection(discoveredAddress);
			
			if (socket == null) {
				
				System.out.println(">>> could not connect to server with address..." + discoveredAddress);
				discoveryStatus = false;
				
			} else {
				
				try {
					
					out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					out.flush();
					in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					System.out.println(">>> socket connected and streams created");
					discoveryStatus = true;
					
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(">>> Could not connect to server... " + discoveredAddress);
					discoveryStatus = false;
				}
			}
			
		} else {
			System.out.println(">>> Could not find server...");
			discoveryStatus = false;
		}

		return discoveryStatus;

	}

	public boolean connectToLocalServer() {

		socket = establishConnection();

		if (socket == null) {
			System.out.println(">>> could not connect to server with localhost...");
			discoveryStatus = false;
			return discoveryStatus;
		}

		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			System.out.println(">>> client connected to local server...");
			discoveryStatus = true;
		} catch (IOException e) {
			e.printStackTrace();
			discoveryStatus = false;
		}

		return discoveryStatus;

	}

	public Socket establishConnection() {
		// establish the connection with server port 11111
		Socket s;
		try {
			InetAddress ip = InetAddress.getByName("localhost");
			s = new Socket(ip, 11111);
			return s;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(">>> could not establish connection... Exiting");
		}
		return null;
	}

	public Socket establishConnection(InetAddress IPAddress) {

		// establish the connection with server port 11111
		Socket s;
		try {
			s = new Socket(IPAddress, 11111);
			return s;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(">>> could not establish connection... Exiting");
		}
		return null;
	}

	public DataTransferPOJO communicateWithServer(String action) {

		DataTransferPOJO received = null;
		if (socket != null && !socket.isClosed()) {
			try {
				player.setAction(action);
				out.reset();
				out.writeObject(player);
				out.flush();

				if (action.equals("Exit")) {
					System.out.println("Closing this connection : " + socket);
					socket.close();
					System.out.println("Connection closed");
					return null;
				}

				Object temp = in.readObject();

				if (temp instanceof DataTransferPOJO) {
					received = (DataTransferPOJO) temp;
					player = received.getAllPlayers().get(player.getName());
				}

			} catch (SocketException se) {
				System.out.println(">>> Connection terminated from server...");
				DisplayWizard.setCurrentMenuType(MenuTypes.MAIN_MENU);
				return null;
			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println(">>> Client is disconnected");
		}
		return received;

	}

	public Player getMyPlayer() {
		return player;
	}

	public void updateMyPlayer(Player myPlayer) {
		player = myPlayer;
	}

}
