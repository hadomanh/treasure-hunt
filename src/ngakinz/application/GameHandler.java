package ngakinz.application;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Data;
import ngakinz.artifact.Artifact;
import ngakinz.artifact.Clue;
import ngakinz.artifact.Treasure;
import ngakinz.enums.MessageHeader;
import ngakinz.model.Response;

@Data
public class GameHandler implements Runnable {

	private Map<ClientHandler, Thread> players;

	private Set<Artifact> checkpoints;

	private int takenTreasures;

	public GameHandler(Map<ClientHandler, Thread> players, Set<Artifact> checkpoints) {
		this.players = players;
		this.checkpoints = checkpoints;
	}

	@Override
	public void run() {

		takenTreasures = 0;

		int numberOfTreasure = getTreasure();

		ClientHandler topPlayer;

		Response response;

		try {

			while (takenTreasures < numberOfTreasure) {

				while (!isAllRequestSent())
					;

				String message = collectMessages();

				topPlayer = getTopPlayer();

				for (Entry<ClientHandler, Thread> p : players.entrySet()) {
					// Return result
					response = new Response(200, MessageHeader.RESULT, message);
					p.getKey().getOut().writeUTF(ApplicationProvider.gson.toJson(response));

					if (takenTreasures == numberOfTreasure 
							|| (topPlayer.getPlayer().getTreasure() > 0 && topPlayer.getPlayer().getTreasure() == numberOfTreasure / 2)) {
						
						response = new Response(200, MessageHeader.FINISH,
								topPlayer.getUsername() + ": " + topPlayer.getPlayer().getTreasure() + " treasure(s)");
						p.getKey().getOut().writeUTF(ApplicationProvider.gson.toJson(response));

						continue;
					}

					// remain treasure
					response = new Response(200, MessageHeader.REMAIN_TREASURE,
							Integer.toString(numberOfTreasure - takenTreasures));
					p.getKey().getOut().writeUTF(ApplicationProvider.gson.toJson(response));

					// re-collect
					response = new Response(200, MessageHeader.RECEIVING, "");
					p.getKey().getOut().writeUTF(ApplicationProvider.gson.toJson(response));

				}

			}

		} catch (IOException e) {
			System.out.println("Interupted");
		}

	}

	private boolean isAllRequestSent() {
		for (Entry<ClientHandler, Thread> p : players.entrySet()) {
			if (p.getKey().getMessage().isEmpty() || p.getKey().getHeader() != MessageHeader.MOVE) {
				return false;
			}
		}
		return true;
	}

	private int getTreasure() {

		return (int) checkpoints.stream().filter(artifact -> artifact.getClass() == Treasure.class).count();

	}

	private String collectMessages() throws IOException {
		StringBuffer sb = new StringBuffer();

		for (Entry<ClientHandler, Thread> p : players.entrySet()) {

			String[] location = p.getKey().getMessage().split(" ");

			p.getKey().getPlayer().setX(Integer.parseInt(location[0]));
			p.getKey().getPlayer().setY(Integer.parseInt(location[1]));

			// add artifact at current location
			for (Artifact a : checkpoints) {
				if (p.getKey().getPlayer().inSameLocation(a) && !p.getKey().getPlayer().collection.contains(a)) {
					a = a.collect(p.getKey().getPlayer().getX(), p.getKey().getPlayer().getY());

					if (a != null) {
						p.getKey().getPlayer().addArtifact(a);

						if (a.getClass() == Clue.class) {
							p.getKey().getOut().writeUTF(ApplicationProvider.gson
									.toJson(new Response(200, MessageHeader.CLUE, ApplicationProvider.gson.toJson(a))));
						} else {

							takenTreasures++;

							p.getKey().getOut().writeUTF(ApplicationProvider.gson.toJson(
									new Response(200, MessageHeader.TREASURE, ApplicationProvider.gson.toJson(a))));
						}

					}

				}

			}

			p.getKey().setMessage("");
			p.getKey().setHeader(null);
		}

		// Share clue
//			for (Entry<ClientHandler, Thread> p : players.entrySet()) {
//				for (Entry<ClientHandler, Thread> pp : players.entrySet()) {
//					if (pp.getKey().getPlayer() != p.getKey().getPlayer()
//							&& pp.getKey().getPlayer().getClass() == NicePlayer.class
//							&& pp.getKey().getPlayer().inSameLocation(p.getKey().getPlayer())) {
//
//						pp.getKey().getPlayer().giveCluesToPerson(p.getKey().getPlayer());
//
//						if (p.getClass() == NicePlayer.class) {
//							p.getKey().getPlayer().giveCluesToPerson(pp.getKey().getPlayer());
//						}
//					}
//				}
//			}

		for (Entry<ClientHandler, Thread> p : players.entrySet()) {

			p.getKey().getOut().writeUTF(ApplicationProvider.gson.toJson(
					new Response(200, MessageHeader.UPDATE, ApplicationProvider.gson.toJson(p.getKey().getPlayer()))));

			sb.append(p.getKey().getUsername());
			sb.append(" (" + p.getKey().getPlayer().getX() + "," + p.getKey().getPlayer().getY() + ")");
			sb.append("\n");
			sb.append("Treasure:");

			if (p.getKey().getPlayer().collection != null) {

				for (Artifact a : p.getKey().getPlayer().collection) {
					if (a.getClass() == Treasure.class) {
						sb.append("\n\t" + ((Treasure) a).getDesc());
					}

				}
			}
			sb.append("\n");
		}

		return sb.toString();

	}

	private ClientHandler getTopPlayer() {

		ClientHandler topPlayer = null;

		for (Entry<ClientHandler, Thread> p : players.entrySet()) {
			if (topPlayer == null || p.getKey().getPlayer().getTreasure() > topPlayer.getPlayer().getTreasure()) {
				topPlayer = p.getKey();
			}
		}

		return topPlayer;

	}

}
