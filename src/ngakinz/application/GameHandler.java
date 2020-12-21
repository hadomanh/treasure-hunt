package ngakinz.application;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Data;

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
					p.getKey().getOut().writeUTF(message);
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
