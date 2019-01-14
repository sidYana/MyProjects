package com.SpaceWars.SocketComm;

import java.net.InetAddress;

public class DiscoveryController {

	private static InetAddress IPAddress = null;

	public static void setIPAddress(InetAddress address) {
		IPAddress = address;
	}

	public static void clearIPAddress() {
		IPAddress = null;
	}

	public static InetAddress getIPAddress() {
		return IPAddress;
	}

}
