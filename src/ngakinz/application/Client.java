package ngakinz.application;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ngakinz.artifact.Artifact;
import ngakinz.artifact.Clue;
import ngakinz.artifact.Treasure;
import ngakinz.enums.MessageHeader;
import ngakinz.model.Request;
import ngakinz.model.Response;
import ngakinz.player.MeanPlayer;
import ngakinz.player.NicePlayer;
import ngakinz.player.Player;

import java.io.*;

public class Client {
	// initialize socket and input output streams
	private Socket socket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	private Scanner scanner = null;
	private Player player;
	private List<Artifact> artifacts = new ArrayList<Artifact>();

	// Constructor to put ip address and port
	public Client(String address, int port) {

		// input
		scanner = new Scanner(System.in).useDelimiter("\n");

		String line = "";

		do {
			System.out.print("Username: ");
			line = scanner.next();
		} while (line.trim().isBlank());

		// Establish a connection
		try {
			socket = new Socket(address, port);

			// input/output stream for socket
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

			// Try to connect server
			Response response = ApplicationProvider.gson.fromJson(in.readUTF(), Response.class);
			System.out.println(response.getMessage());

			// Access denied
			if (response.getHeader() == MessageHeader.DENY) {
				return;
			}

			Request request;

			int option = 1;
			Artifact founded;

			request = new Request(MessageHeader.USERNAME, line);
			this.out.writeUTF(ApplicationProvider.gson.toJson(request));

			// keep reading
			RECEIVE: do {

				response = ApplicationProvider.gson.fromJson(in.readUTF(), Response.class);

				switch (response.getHeader()) {

				case REMAIN_TREASURE:
					System.out.println("** Remain treasures: " + response.getMessage() + " treasure(s)");
					break;

				case NICE_PLAYER:
					System.out.println("** Player generated");
					player = ApplicationProvider.gson.fromJson(response.getMessage(), NicePlayer.class);
					System.out.println(player);
					System.out.println("**");
					break;

				case MEAN_PLAYER:
					System.out.println("** Player generated");
					player = ApplicationProvider.gson.fromJson(response.getMessage(), MeanPlayer.class);
					System.out.println(player);
					System.out.println("**");
					break;

				case CLUE:
					founded = ApplicationProvider.gson.fromJson(response.getMessage(), Clue.class);
					artifacts.add(founded);
					System.out.println("** Clue founded: " + response.getMessage());
					break;

				case TREASURE:
					founded = ApplicationProvider.gson.fromJson(response.getMessage(), Treasure.class);
					artifacts.add(founded);
					System.out.println("** Treasure founded: " + ((Treasure) founded).getDesc());
					break;

				case UPDATE:
					player = ApplicationProvider.gson.fromJson(
							response.getMessage().replaceFirst("\"collection\":\\[[^\\]]*\\]", "\"collection\":[]"),
							player.getClass());
					break;

				case START:
					System.out.println("** Game start...");
				case RECEIVING:
					System.out.println(player);
					do {
						System.out.println("[1]. Move");
						System.out.println("[0]. Exit");
						System.out.print("Option: ");
						line = scanner.next();
					} while (!line.matches("[0-1]"));

					option = Integer.parseInt(line);

					switch (option) {
					case 0: {
						request = new Request(MessageHeader.EXIT, "Boring game...");
						out.writeUTF(ApplicationProvider.gson.toJson(request));
						break RECEIVE;
					}

					case 1:
						System.out.print("Location: ");
						line = scanner.next().trim();

						while (!isValidInput(line)) {
							System.out.println("Usage: <x> <space> <y>");
							System.out.print("Location: ");
							line = scanner.next().trim();
						}

						request = new Request(MessageHeader.MOVE, line);
						out.writeUTF(ApplicationProvider.gson.toJson(request));
						break;

					default:
						break;
					}
					
					break;

				case WAITING:
					System.out.println(response.getMessage());
					break;

				case FINISH:
					System.out.print("Winner is ");
					System.out.println(response.getMessage());
					break RECEIVE;

				case RESULT:
					System.out.println("** Round result: ");
					System.out.print(response.getMessage());
					System.out.println("**");
					break;

				default:
					break;
				}

			} while (option != 0);

			// close the connection
			in.close();
			out.close();
			socket.close();
			scanner.close();
		} catch (IOException e) {
			System.out.println("Disconnected from server...");
		}
	}

	public boolean isValidInput(String line) {
		if (!line.matches("^\\d+\\s\\d+$")) {
			return false;
		}

		String[] location = line.split(" ");
		return player.isPossible(Integer.parseInt(location[0]), Integer.parseInt(location[1]));
	}

	public static void main(String args[]) {
		new Client(ApplicationProvider.HOST, ApplicationProvider.PORT);
	}
}