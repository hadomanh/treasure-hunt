package ngakinz.application;

import java.net.*;
import java.util.Scanner;

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

	// Constructor to put ip address and port
	public Client(String address, int port) {

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

			// string to read message from input
			scanner = new Scanner(System.in).useDelimiter("\n");
			String line = "";
			int option = 1;

			// keep reading
			do {
				response = ApplicationProvider.gson.fromJson(in.readUTF(), Response.class);
				System.out.println(response.getMessage());

				switch (response.getHeader()) {

				case NICE_PLAYER:
					player = ApplicationProvider.gson.fromJson(response.getMessage(), NicePlayer.class);
					System.out.println(player);
					break;

				case MEAN_PLAYER:
					player = ApplicationProvider.gson.fromJson(response.getMessage(), MeanPlayer.class);
					System.out.println(player);
					break;

				case START:
				case COLLECTING:
					do {
						System.out.println("[1]. Move");
						System.out.println("[0]. Exit");
						System.out.print("Option: ");
						option = scanner.nextInt();
					} while (option < 0 || option > 1);

					Request request;

					if (option == 0) {
						request = new Request(MessageHeader.EXIT, "Boring game...");
						out.writeUTF(ApplicationProvider.gson.toJson(request));
						break;
					}

					System.out.print("Location: ");
					line = scanner.next().trim();

					while (!line.matches("^\\d+\\s\\d+$")) {
						System.out.println("Usage: <x> <space> <y>");
						System.out.print("Location: ");
						line = scanner.next().trim();
					}

					request = new Request(MessageHeader.MOVE, line);
					out.writeUTF(ApplicationProvider.gson.toJson(request));
					break;

				case WAITING:
				case RESULT:
					break;

				default:
					throw new IllegalArgumentException("Unexpected value: " + response.getHeader());
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

	public static void main(String args[]) {
		new Client(ApplicationProvider.HOST, ApplicationProvider.PORT);
	}
}