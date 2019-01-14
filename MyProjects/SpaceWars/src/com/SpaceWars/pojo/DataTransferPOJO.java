package com.SpaceWars.pojo;

import java.io.Serializable;
import java.util.Map;

public class DataTransferPOJO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Player> allPlayers;

	public Map<String, Player> getAllPlayers() {
		return allPlayers;
	}

	public void setAllPlayers(Map<String, Player> allPlayers) {

		int timer = 0;
		for (String name : allPlayers.keySet()) {
			Player hostingPlayer = allPlayers.get(name);
			if (hostingPlayer.isHosting()) {
				timer = hostingPlayer.getTimer();
				for (String playerName : allPlayers.keySet()) {
					Player player = allPlayers.get(playerName);
					player.setTimer(timer);
					allPlayers.put(playerName, player);
				}
				this.allPlayers = allPlayers;
				break;
			}
		}
		
	}

}
