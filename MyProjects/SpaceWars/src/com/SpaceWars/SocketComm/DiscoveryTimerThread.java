package com.SpaceWars.SocketComm;

public class DiscoveryTimerThread implements Runnable {

	DiscoveryServer discoveryServer;
	DiscoveryClient discoveryClient;

	private String timerType = "NONE";

	public DiscoveryTimerThread(DiscoveryServer discoveryServer) {// for discovery server
		this.timerType = "DISCOVERY_SERVER";
	}

	public DiscoveryTimerThread(DiscoveryClient discoveryClient) {// for discovery client
		this.timerType = "DISCOVERY_CLIENT";
	}

	@Override
	public void run() {
		try {
			switch (timerType) {
			case "DISCOVERY_SERVER":
				Thread.sleep(30000);
				DiscoveryServer.stopDiscovery();
				break;
			case "DISCOVERY_CLIENT":
				Thread.sleep(10000);
				DiscoveryClient.getInstance().stopDiscovery();
				break;
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
