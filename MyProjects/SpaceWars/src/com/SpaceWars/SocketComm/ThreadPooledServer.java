package com.SpaceWars.SocketComm;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.SpaceWars.GameData.GameData;

public class ThreadPooledServer implements Runnable {

	private static int i = 0;
	private final int MAX_PLAYERS_ALLOWED = 4;

	protected int serverPort = 11111;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	protected ExecutorService threadPool = Executors.newFixedThreadPool(MAX_PLAYERS_ALLOWED);

	private int totalConnectedClients;
	
	public ThreadPooledServer() {
		GameData.setCloseAllClients(false);
		totalConnectedClients = 0;
	}

	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		while (!isStopped()) {
			
			if(totalConnectedClients >= MAX_PLAYERS_ALLOWED) {
				isStopped = true;
				continue;
			}
			
			Socket clientSocket = null;
			ObjectOutputStream out = null;
			ObjectInputStream in = null;
			try {
				clientSocket = this.serverSocket.accept();
				out = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
				out.flush();
				in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
				this.threadPool.execute(new ClientHandler(clientSocket, "client" + i, in, out));
				totalConnectedClients++;
			} catch (IOException e) {
				if (isStopped()) {
					System.out.println(">>> Server Stopped.");
					break;
				}
				throw new RuntimeException(">>> Error accepting client connection", e);
			}
		}
		this.threadPool.shutdown();// stop accepting tasks
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stopServer() {
		this.isStopped = true;
		GameData.setCloseAllClients(true);
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException(">>> Error stopping server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
			System.out.println(">>> Server socket created at port:" + serverPort);
		} catch (IOException e) {
			throw new RuntimeException(">>> Cannot open port:" + serverPort, e);
		}
	}

}