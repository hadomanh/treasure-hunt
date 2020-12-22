package ngakinz.application;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Data;
import ngakinz.enums.MessageHeader;
import ngakinz.model.Response;

@Data
public class GameHandler implements Runnable {
	
	private Map<ClientHandler, Thread> players;
	
	public GameHandler(Map<ClientHandler, Thread> players) {
		this.players = players;
	}

	@Override
	public void run() {
		while(true) {
			while(!isAllRequestSent());
			
			String message = collectMessages();
			
			for (Entry<ClientHandler, Thread> p : players.entrySet()) {
				try {
					// Return result
					Response response = new Response(200, MessageHeader.RESULT, message);
					p.getKey().getOut().writeUTF(ApplicationProvider.gson.toJson(response));
					
					// re-collect
					response = new Response(200, MessageHeader.COLLECTING, "");
					p.getKey().getOut().writeUTF(ApplicationProvider.gson.toJson(response));
					
				} catch (IOException e) {
					System.out.println("..." + e.toString());
				}
			}
		}
		
	}
	
	private boolean isAllRequestSent() {
		for (Entry<ClientHandler, Thread> p : players.entrySet()) {
			if (p.getKey().getMessage().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	private String collectMessages() {
		StringBuffer sb = new StringBuffer();
		
		for (Entry<ClientHandler, Thread> p : players.entrySet()) {
			sb.append(p.getKey().getMessage() + "\n");
			p.getKey().setMessage("");
		}
		
		return sb.toString();
		
	}

}
