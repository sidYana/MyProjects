package com.SpaceWars.SocketComm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DiscoveryServer implements Runnable {

	DatagramSocket socket;

	private static boolean isStopped = false;

	@Override
	public void run() {

		try {
			// Keep a socket open to listen to all the UDP trafic that is destined for this
			// port
			socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (!isStopped) {
				System.out.println(getClass().getName() + ">>> Ready to receive broadcast packets!");

				// Receive a packet
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
				socket.receive(packet);

				// Packet received
				System.out.println(">>> " + getClass().getName() + ">>> Discovery packet received from: "
						+ packet.getAddress().getHostAddress());
				System.out.println(">>> " + getClass().getName() + ">>> Packet received; data: " + new String(packet.getData()));

				// See if the packet holds the right command (message)
				String message = new String(packet.getData()).trim();
				if (message.equals("DISCOVER_FUIFSERVER_REQUEST")) {
					byte[] sendData = "DISCOVER_FUIFSERVER_RESPONSE".getBytes();

					// Send a response
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(),
							packet.getPort());
					socket.send(sendPacket);

					System.out.println(">>> " + 
							getClass().getName() + ">>> Sent packet to: " + sendPacket.getAddress().getHostAddress());
				}
			}
		} catch (IOException ex) {
			System.out.println(">>> discovery stopped... " + ex);
		}
		isStopped = false;
	}

	public static void stopDiscovery() {
		System.out.println(">>> trying to stop discovery...");
		isStopped = true;
		if (!DiscoveryServerThreadHolder.INSTANCE.socket.isClosed()) {
			DiscoveryServerThreadHolder.INSTANCE.socket.close();
			System.out.println(">>> discovery server stopped");
		}else {
			System.out.println(">>> discovery server already stopped...");
		}
	}

	public static DiscoveryServer getInstance() {
		return DiscoveryServerThreadHolder.INSTANCE;
	}

	private static class DiscoveryServerThreadHolder {
		private static final DiscoveryServer INSTANCE = new DiscoveryServer();
	}

}
