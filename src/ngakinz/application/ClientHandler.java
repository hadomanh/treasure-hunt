package ngakinz.application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import lombok.Data;
import ngakinz.enums.MessageHeader;
import ngakinz.model.Request;
import ngakinz.model.Response;

@Data
public class ClientHandler implements Runnable {
	
	private Socket socket;
	
	private DataInputStream in;
	
	private DataOutputStream out;
	
	private volatile String message;
	
	public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.message = "";
	}

	@Override
	public void run() {
		try {
			
			Response response = new Response(200, MessageHeader.START, "Start...");
			out.writeUTF(ApplicationProvider.gson.toJson(response));
			
			System.out.println("Client accepted: " + socket.getPort());

			// reads message from client until "Over" is sent
			while (!message.equalsIgnoreCase("Over")) {
				Request request = ApplicationProvider.gson.fromJson(in.readUTF(), Request.class);
				message = request.getMessage();
				System.out.println("From port " + socket.getPort() + ": " + message);
			}

			System.out.println("Disconnected: " + socket.getPort());
			socket.close();
			in.close();
			out.close();
			
		} catch (IOException e) {
			System.out.println("Interupted: " + socket.getPort());
		}
		
		
	}

}
