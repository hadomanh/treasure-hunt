package ngakinz.simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class TreasureHuntSim {
	
	private static Set<Artifact> readCheckPoints(Scanner scanner) {
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
	
	private static List<Player> readPlayers(Scanner scanner) {
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

	private static void simulate(String filename) throws FileNotFoundException {

		Scanner scanner = new Scanner(new FileInputStream(filename)).useDelimiter("\\s");

		Set<Artifact> checkPoints = readCheckPoints(scanner);
		
		List<Player> players = readPlayers(scanner);

		scanner.close();

		boolean loop = false;
		do {

			loop = false;

			for (Player p : players) {

				p.move();

				// add artifact at current location
				for (Artifact a : checkPoints) {
					if (p.inSameLocation(a) && !p.collection.contains(a)) {
						a = a.collect(p.getX(), p.getY());

						if (a != null) {
							p.addArtifact(a);
						}

					}

				}

				// update destination
				if (!p.update()) {
					for (Artifact aa : p.collection) {
						if (aa.getClass() == Clue.class && p.getX() == ((Clue) aa).getXDest()
								&& p.getYDest() == ((Clue) aa).getYDest() && !((Clue) aa).isVisited()) {
							((Clue) aa).setVisited(true);
							continue;
						}
						if (aa.getClass() == Clue.class && !((Clue) aa).isVisited()) {
							p.setXDest(((Clue) aa).getXDest());
							p.setYDest(((Clue) aa).getYDest());
							break;
						}
					}
				}

				if (p.update()) {
					loop = true;
				}

			}

			for (Player p : players) {
				for (Player pp : players) {
					if (pp != p && pp.getClass() == NicePlayer.class && pp.inSameLocation(p)) {
						pp.giveCluesToPerson(p);
						if (p.getClass() == NicePlayer.class) {
							p.giveCluesToPerson(pp);
						}
					}
				}
			}

			for (Player p : players) {
				System.out.println(p.toString());
			}

		} while (loop);

	}

	public static void main(String[] args) throws IOException {

		simulate("2.txt");

	}

}
