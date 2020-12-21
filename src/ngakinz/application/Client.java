package ngakinz.application;

//A Java program for a Client 
import java.net.*;
import java.util.Scanner;

import ngakinz.enums.MessageHeader;
import ngakinz.model.Request;
import ngakinz.model.Response;

import java.io.*;

public class Client {
	// initialize socket and input output streams
	private Socket socket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	private Scanner scanner = null;

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

			// Waiting at lobby
			System.out.println("Waiting other players...");

			// game start
			response = ApplicationProvider.gson.fromJson(in.readUTF(), Response.class);
			System.out.println(response.getMessage());

			// string to read message from input
			scanner = new Scanner(System.in).useDelimiter("\n");
			String line = "";

			// keep reading until "Over" is input
			do {

				System.out.print("Message: ");
				line = scanner.next();
				line = line.trim();

				while (!line.matches("^\\d+\\s\\d+$")) {
					System.out.println("Usage: <x> <space> <y>");
					System.out.print("Message: ");
					line = scanner.next();
					line = line.trim();
				}

				Request request = new Request(MessageHeader.MOVE, line);
				out.writeUTF(ApplicationProvider.gson.toJson(request));

				System.out.println("Waiting response...");
				System.out.println(in.readUTF());

			} while (!line.equalsIgnoreCase("over"));

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