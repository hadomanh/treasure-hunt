package ngakinz.application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import lombok.Data;
import ngakinz.enums.MessageHeader;
import ngakinz.model.Request;
import ngakinz.model.Response;
import ngakinz.player.Player;

@Data
public class ClientHandler implements Runnable {

	private Socket socket;

	private DataInputStream in;

	private DataOutputStream out;

	private volatile String message;

	private volatile MessageHeader header;

	private volatile Player player;

	private volatile String username;

	public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out, Player player) {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.message = "";
		this.header = null;
		this.player = player;
		this.username = player.getName();
	}

	@Override
	public void run() {
		try {

			Response response;

			System.out.println("Client accepted: " + socket.getPort());

			Request request;

			// read message from client until "EXIT" signal is sent
			RECEIVE: do {
				request = ApplicationProvider.gson.fromJson(in.readUTF(), Request.class);
				message = request.getMessage();
				header = request.getHeader();

				System.out.println("From port " + socket.getPort() + ": " + message);

				switch (request.getHeader()) {
				case EXIT:
					break RECEIVE;

				case USERNAME:
					this.username = message;

					response = new Response(200, MessageHeader.START, player.toString());
					out.writeUTF(ApplicationProvider.gson.toJson(response));
					break;

				default:
					response = new Response(200, MessageHeader.WAITING, "** Waiting other players...");
					out.writeUTF(ApplicationProvider.gson.toJson(response));
					break;
				}

			} while (true);

			System.out.println("Disconnected: " + socket.getPort());
			socket.close();
			in.close();
			out.close();

		} catch (IOException e) {
			System.out.println("Interupted: " + socket.getPort());
		}

	}

}
