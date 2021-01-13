package ngakinz.application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Thread.State;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import ngakinz.artifact.Artifact;
import ngakinz.enums.MessageHeader;
import ngakinz.model.Response;
import ngakinz.player.NicePlayer;
import ngakinz.player.Player;

public class Server {

	// initialize
	private ServerSocket server = null;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;

	private Map<ClientHandler, Thread> players = new HashMap<ClientHandler, Thread>();

	private Thread gameThread;

	// constructor with port
	public Server(int port) throws IOException {
		// starts server and waits for a connection
		System.out.println("Server started");
		System.out.println("Waiting for a client ...");

		server = new ServerSocket(port);

		while (true) {

			socket = server.accept();
			System.out.println("New client from port: " + socket.getPort());

			// takes input from the client socket
			in = new DataInputStream(socket.getInputStream());

			out = new DataOutputStream(socket.getOutputStream());

			if (getPlayerAmount() < ApplicationProvider.CAPACITY) {
				Response response = new Response(200, MessageHeader.ALLOW, "Connected");
				out.writeUTF(ApplicationProvider.gson.toJson(response));

				Player p = ApplicationProvider.generator.getPlayer();
				addPlayer(socket, in, out, p);

				if (p.getClass() == NicePlayer.class) {
					response = new Response(200, MessageHeader.NICE_PLAYER, ApplicationProvider.gson.toJson(p));
				} else {
					response = new Response(200, MessageHeader.MEAN_PLAYER, ApplicationProvider.gson.toJson(p));
				}

				out.writeUTF(ApplicationProvider.gson.toJson(response));

				if (getPlayerAmount() == ApplicationProvider.CAPACITY) {
					startGame();
				} else {
					response = new Response(200, MessageHeader.WAITING, "** Waiting other players...");
					out.writeUTF(ApplicationProvider.gson.toJson(response));
				}

			} else {
				Response response = new Response(403, MessageHeader.DENY, "Access denied");
				out.writeUTF(ApplicationProvider.gson.toJson(response));
			}
		}

	}

	private void startGame() {

		System.out.println("Created:");
		for (Artifact a : ApplicationProvider.generator.getArtifacts()) {
			System.out.println(a.getClass().getSimpleName() + " (" + a.getX() + "," + a.getY() + ")");
		}

		for (Entry<ClientHandler, Thread> p : players.entrySet()) {
			if (p.getValue().getState() == State.NEW) {
				p.getValue().start();
			}
		}

		this.gameThread = new Thread(new GameHandler(players, ApplicationProvider.generator.getArtifacts()));
		this.gameThread.start();

		while (this.gameThread.getState() != State.TERMINATED)
			;
		ApplicationProvider.reset();

	}

	private int getPlayerAmount() {

		players = players.entrySet().stream().filter(x -> x.getValue().getState() != State.TERMINATED)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

		return players.size();
	}

	private void addPlayer(Socket socket, DataInputStream in, DataOutputStream out, Player player) {
		ClientHandler c = new ClientHandler(socket, in, out, player);
		Thread t = new Thread(c);
		this.players.put(c, t);
	}

	public static void main(String args[]) throws IOException {
		new Server(ApplicationProvider.PORT);
	}
}