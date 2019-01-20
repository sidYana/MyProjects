package com.SpaceWars.pojo;

import java.io.Serializable;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String clientNum;

	private Ship myShip;

	private String action = "NONE";

	private boolean isHosting = false;

	private int timer = 0;

	public Player(String username, int width, int height, String shipType) {
		this.setName(username);
		this.setMyShip(new Ship(width, height, shipType));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Ship getMyShip() {
		return myShip;
	}

	public void setMyShip(Ship myShip) {
		this.myShip = myShip;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getClientNum() {
		return clientNum;
	}

	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}

	public boolean isHosting() {
		return isHosting;
	}

	public void setHosting(boolean isHosting) {
		this.isHosting = isHosting;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

}
