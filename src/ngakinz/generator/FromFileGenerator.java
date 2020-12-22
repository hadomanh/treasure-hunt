package ngakinz.generator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ngakinz.artifact.Artifact;
import ngakinz.artifact.Clue;
import ngakinz.artifact.Treasure;
import ngakinz.player.MeanPlayer;
import ngakinz.player.NicePlayer;
import ngakinz.player.Player;

public class FromFileGenerator implements GameGenerator {
	
	private Set<Artifact> checkPoints;
	
	private List<Player> players;
	
	private int currentPlayer = -1;
	
	public FromFileGenerator(String filename) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(filename)).useDelimiter("\\s");

		checkPoints = readCheckPoints(scanner);
		
		players = readPlayers(scanner);

		scanner.close();
	}

	@Override
	public Set<Artifact> getArtifacts() {
		return checkPoints;
	}

	@Override
	public List<Player> getPlayers() {
		return players;
	}

	
	private Set<Artifact> readCheckPoints(Scanner scanner) {
		Set<Artifact> checkPoints = new HashSet<Artifact>();
		int artifactCount = scanner.nextInt();
		scanner.nextLine();

		String id, type, desc;
		int x, y, xDest, yDest, instances;

		for (int i = 0; i < artifactCount; i++) {
			id = scanner.next();
			x = scanner.nextInt();
			y = scanner.nextInt();
			type = scanner.next();

			if ("clue".equals(type)) {
				xDest = scanner.nextInt();
				yDest = scanner.nextInt();

				checkPoints.add(new Clue(id, x, y, xDest, yDest));
			} else if ("treasure".equals(type)) {
				instances = scanner.nextInt();
				desc = scanner.nextLine().trim();

				checkPoints.add(new Treasure(id, x, y, instances, desc));
			}

		}
		
		return checkPoints;
		
	}
	
	private List<Player> readPlayers(Scanner scanner) {
		List<Player> players = new ArrayList<Player>();

		String type, name;
		int x, y, xDest, yDest, speed;
		
		int playerCount = scanner.nextInt();
		for (int i = 0; i < playerCount; i++) {
			name = scanner.next();
			type = scanner.next();
			speed = scanner.nextInt();
			x = scanner.nextInt();
			y = scanner.nextInt();
			xDest = scanner.nextInt();
			yDest = scanner.nextInt();

			if ("nice".equals(type)) {
				players.add(new NicePlayer(name, speed, x, y, xDest, yDest));
			} else {
				players.add(new MeanPlayer(name, speed, x, y, xDest, yDest));
			}

		}
		
		return players;
		
	}

	@Override
	public Player getPlayer() {
		currentPlayer %= players.size();
		currentPlayer ++;
		return players.get(currentPlayer);
	}

}
