package com.SpaceWars.SocketComm;

import com.SpaceWars.Camera.CameraControl;
import com.SpaceWars.GameData.PlayerData;
import com.SpaceWars.pojo.DataTransferPOJO;
import com.SpaceWars.pojo.Player;

import processing.core.PVector;

public class SocketCommunicationUtil {

	private static ThreadPooledServer server = null;
	private static Thread discoveryServerThread = null;
	private static Thread timerThreadForDiscovery = null;
	private static Thread serverThread = null;
	private static Client client = null;

	private static CameraControl myCamera = null;
	private static PlayerData playerData = null;

	private static final String SERVER_THREAD_NAME = "MAIN_SERVER_THREAD";
	private static final String DISCOVERY_SERVER_THREAD_NAME = "DISCOVERY_SERVER_THREAD";
	private static final String DISCOVERY_SERVER_TIMER_THREAD_NAME = "DISCOVERY_SERVER_TIMER_THREAD";

	public static boolean startServer() {

		// Start Main server thread
		if (serverThread == null || !serverThread.isAlive()) {
			server = new ThreadPooledServer();
			serverThread = new Thread(server, SERVER_THREAD_NAME);
			serverThread.start();
			System.out.println(">>> server started");
			return true;
		} else {
			System.out.println(">>> server is still alive");
			return false;
		}

	}

	public static boolean stopServer() {

		if (serverThread != null && serverThread.isAlive()) {
			System.out.println(">>> Trying to stop server");
			server.stopServer();
			return true;
		} else {
			System.out.println(">>> server is already stopped");
			return false;
		}

	}

	public static boolean startDiscoveryServer() {

		// Start discovery server thread
		if (discoveryServerThread == null || !discoveryServerThread.isAlive()) {
			discoveryServerThread = new Thread(DiscoveryServer.getInstance(), DISCOVERY_SERVER_THREAD_NAME);
			discoveryServerThread.start();

			timerThreadForDiscovery = new Thread(new DiscoveryTimerThread(DiscoveryServer.getInstance()),
					DISCOVERY_SERVER_TIMER_THREAD_NAME);
			timerThreadForDiscovery.start();
			System.out.println(">>> discovery server started");
			return true;
		} else {
			System.out.println(">>> discovery is still alive");
			return false;
		}

	}

	public static boolean stopDiscoveryServer() {

		if (discoveryServerThread != null && discoveryServerThread.isAlive()) {
			DiscoveryServer.stopDiscovery();
			return true;
		} else {
			System.out.println(">>> discovery is already stopped");
			return false;
		}

	}

	public static boolean connectToServer(PVector screenSize) {

		if (myCamera == null) {
			playerData = new PlayerData();
			myCamera = new CameraControl((int) screenSize.x, (int) screenSize.y);
		}

		int x_coords = (int) myCamera.getWorldSize().x;
		int y_coords = (int) myCamera.getWorldSize().y;
		String playerName = playerData.getPlayerNameFromFile();
		String shipType = playerData.getPlayerShipTypeFromFile();

		client = new Client(x_coords, y_coords, playerName, shipType);

		if (serverThread != null && serverThread.isAlive()) {
			// if the player is the host then connect to local host
			return client.connectToLocalServer();
		} else {
			// if the player is not the host then connect to host server in the network
			return client.checkForServerInNetwork();
		}

	}

	public static DataTransferPOJO communicateWithServer(Player player, String action) {
		if (client != null) {
			client.updateMyPlayer(player);
			return client.communicateWithServer(player.getAction());
		}
		return null;
	}

	public static void disconnectClient() {
		if (client != null) {
			client.communicateWithServer("Exit");
		} else {
			System.out.println(">>> Client already disconnected");
		}
	}

	public static boolean isLocalServerRunning() {
		return (serverThread == null || !serverThread.isAlive()) ? false : true;
	}

	public static Client getClient() {
		return client;
	}

	public static void updatePlayer(Player player) {
		client.updateMyPlayer(player);
	}
	
}
