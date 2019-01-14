package com.SpaceWars.SocketComm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class DiscoveryClient {

	private DatagramSocket dSocket;

	private InetAddress discoveredServerAddress = null;

	public InetAddress startDiscovery() {
		// Find the server using UDP broadcast
		discoveredServerAddress = null;
		try {
			// Open a random port to send the package
			dSocket = new DatagramSocket();
			dSocket.setBroadcast(true);

			byte[] sendData = "DISCOVER_FUIFSERVER_REQUEST".getBytes();

			// Try the 255.255.255.255 first
			try {
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						InetAddress.getByName("255.255.255.255"), 8888);
				dSocket.send(sendPacket);
				System.out.println(
						DiscoveryClient.class.getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
			} catch (Exception e) {
			}

			// Broadcast the message over all the network interfaces
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue; // Don't want to broadcast to the loopback interface
				}

				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}

					// Send the broadcast package!
					try {
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
						dSocket.send(sendPacket);
					} catch (Exception e) {
					}

					System.out.println(">>> " + DiscoveryClient.class.getName() + ">>> Request packet sent to: "
							+ broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
				}
			}

			System.out.println(DiscoveryClient.class.getName()
					+ ">>> Done looping over all network interfaces. Now waiting for a reply!");

			// Wait for a response
			byte[] recvBuf = new byte[15000];
			DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
			dSocket.receive(receivePacket);

			// We have a response
			System.out.println(DiscoveryClient.class.getName() + ">>> Broadcast response from server: "
					+ receivePacket.getAddress().getHostAddress());

			// Check if the message is correct
			String message = new String(receivePacket.getData()).trim();
			if (message.equals("DISCOVER_FUIFSERVER_RESPONSE")) {
				// DO SOMETHING WITH THE SERVER'S IP (for example, store it in your controller)
				discoveredServerAddress = receivePacket.getAddress();
			}

			// Close the port!
			dSocket.close();
		} catch (IOException ex) {

			System.out.println("discovery client closed... " + ex);

		}
		System.out.println("discovery client exited...");
		return discoveredServerAddress;
	}

	public void stopDiscovery() {
		System.out.println("trying to stop discovery...");
		if (!DiscoveryClientThreadHolder.INSTANCE.dSocket.isClosed()) {
			DiscoveryClientThreadHolder.INSTANCE.dSocket.close();
		} else {
			System.out.println("discovery client already closed...");
		}
	}

	public static DiscoveryClient getInstance() {
		return DiscoveryClientThreadHolder.INSTANCE;
	}

	private static class DiscoveryClientThreadHolder {

		private static final DiscoveryClient INSTANCE = new DiscoveryClient();
	}

}
