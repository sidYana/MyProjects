package com.SpaceWars.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.SpaceWars.pojo.Asteroid;
import com.SpaceWars.pojo.Bullet;
import com.SpaceWars.pojo.DataTransferPOJO;
import com.SpaceWars.pojo.Player;

import processing.core.PVector;

public class GameData extends PVector {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Map<String, Player> allPlayers = new HashMap<>();
	private static Map<String, PVector> playerLocations = new HashMap<>();
	private static Map<String, List<Bullet>> playerBulletLocations = new HashMap<>();
	private static List<Asteroid> allAsteroids = new ArrayList<>();
	private static boolean closeAllClients = false;
	private static DataTransferPOJO data = new DataTransferPOJO();

	public static void createAsteroids() {

	}

	public List<Asteroid> getAllAsteroids() {
		return allAsteroids;
	}

	public static void addNewPlayer(Player player) {
		allPlayers.put(player.getName(), player);
	}

	public static Player getPlayer(String playerName) {
		return allPlayers.get(playerName);
	}

	public static DataTransferPOJO getAllConnectedPlayers() {

		for (String playerName : allPlayers.keySet()) {
			playerLocations.put(playerName, allPlayers.get(playerName).getMyShip().getLocation());
			playerBulletLocations.put(playerName, allPlayers.get(playerName).getMyShip().getBullets());
		}
		checkIfHitByBullet();
		data.setAllPlayers(allPlayers);
		return data;
	}

	public static void checkIfHitByBullet() {

		for (String playerName : allPlayers.keySet()) {

			PVector location = playerLocations.get(playerName);

			Set<String> bulletSet = playerBulletLocations.keySet();

			for (String otherPlayer : bulletSet) {

				if (!playerName.equals(otherPlayer)) {

					List<Bullet> bullets = playerBulletLocations.get(otherPlayer);

					for (Bullet bullet : bullets) {

						if (dist(location, bullet.getLocation()) < 50
								&& !allPlayers.get(playerName).getMyShip().getShieldStatus()) {

							int bulletDamage = bullet.getDamageDealt();
							allPlayers.get(playerName).getMyShip().applyDamage(bulletDamage);

							if (allPlayers.get(playerName).getMyShip().getHealth() <= 0) {
								allPlayers.get(playerName).getMyShip().setDead(true);
							} else {
								bullet.setBulletHit(true);
							}

						} else if (dist(location, bullet.getLocation()) < 70
								&& allPlayers.get(playerName).getMyShip().getShieldStatus()) {

							bullet.setBulletHit(true);

						}

					}

				}

			}

		}

	}

	public static void removePlayer(Player player) {
		if (allPlayers.containsKey(player.getName())) {
			allPlayers.remove(player.getName());
			playerLocations.remove(player.getName());
			playerBulletLocations.remove(player.getName());
		}

		System.out.println(allPlayers);

	}

	public static boolean isCloseAllClients() {
		return closeAllClients;
	}

	public static void setCloseAllClients(boolean closeAllClients) {
		GameData.closeAllClients = closeAllClients;
	}

}
