package com.SpaceWars.SocketComm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	// counter for clients
	static int i = 0;

	ServerSocket serverSocket;

	public Server() {

		try {
			serverSocket = new ServerSocket(11111);
			System.out.println("Server created... Listening for clients");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Socket clientSocket;

		// running infinite loop for getting
		// client request
		while (true) {
			// Accept the incoming request
			try {
				clientSocket = serverSocket.accept();

				System.out.println("New client request received : " + clientSocket);

				// obtain input and output streams
				ObjectOutputStream oos = new ObjectOutputStream(
						new BufferedOutputStream(clientSocket.getOutputStream()));
				oos.flush();
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));

				System.out.println("Creating a new handler for this client...");

				// Create a new handler object for handling this request.
				ClientHandler mtch = new ClientHandler(clientSocket, "Client" + i, ois, oos);

				// Create a new Thread with this object.
				Thread t = new Thread(mtch);

				System.out.println("Adding this client to active client list");

				// start the thread.
				t.start();

				// increment i for new client.
				// i is used for naming only, and can be replaced
				// by any naming scheme
				i++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		// server is listening on port 1234
		new Server();
	}
}
