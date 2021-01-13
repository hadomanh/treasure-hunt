package ngakinz.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.github.javafaker.Faker;

import lombok.Data;
import ngakinz.application.ApplicationProvider;
import ngakinz.artifact.Artifact;
import ngakinz.artifact.Clue;
import ngakinz.artifact.Treasure;
import ngakinz.player.MeanPlayer;
import ngakinz.player.NicePlayer;
import ngakinz.player.Player;

@Data
public class RandomGenerator implements GameGenerator {

	private int maxPlayer;

	private int maxTreasure;

	private int xLimit;

	private int yLimit;
	
	private int currentPlayer = 0;

	private final Random random = new Random();

	private final Faker faker = new Faker();

	private Set<Artifact> artifacts;

	private List<Artifact> randomArtifacts;

	private List<Player> players;
	
	public RandomGenerator(int maxPlayer, int maxTreasure, int xLimit, int yLimit) {
		this.maxPlayer = maxPlayer;
		this.maxTreasure = maxTreasure;
		this.xLimit = xLimit;
		this.yLimit = yLimit;
		genArtifacts();
		genPlayers();
	}

	private int randomX() {
		return random.nextInt(xLimit);
	}

	private int randomY() {
		return random.nextInt(yLimit);
	}

	private Clue randomClue() {
		Artifact a;
		do {
			a = randomArtifacts.get(random.nextInt(randomArtifacts.size()));
		} while (a.getClass() != Clue.class);

		return (Clue) a;
	}

	@Override
	public Set<Artifact> getArtifacts() {

		return artifacts;
	}

	@Override
	public List<Player> getPlayers() {
		

		return players;
	}

	@Override
	public Player getPlayer() {
		currentPlayer ++;
		currentPlayer %= players.size();
		return players.get(currentPlayer);
	}
	
	private void genPlayers() {
		players = new ArrayList<Player>();
		Clue c;

		for (int i = 0; i < maxPlayer; i++) {
			c = randomClue();
			
			if (random.nextBoolean()) {
				players.add(new NicePlayer(faker.gameOfThrones().character(), random.nextInt(ApplicationProvider.MAX_SPEED) + 1, randomX(), randomY(),
						c.getX(), c.getY()));
			} else {
				players.add(new MeanPlayer(faker.gameOfThrones().character(), random.nextInt(ApplicationProvider.MAX_SPEED) + 1, randomX(), randomY(),
						c.getX(), c.getY()));
			}
			
			
		}
	}
	
	private void genArtifacts() {
		
		randomArtifacts = new ArrayList<Artifact>();

		for (int i = 0; i < maxTreasure; i++) {
			randomArtifacts.add(new Treasure(faker.color().name(), randomX(), randomY(), random.nextInt(maxPlayer) + 1,
					faker.gameOfThrones().house()));
		}

		Treasure t;

		for (int i = 0; i < maxTreasure; i++) {
			t = (Treasure) randomArtifacts.get(i);
			randomArtifacts.add(new Clue(faker.color().name(), randomX(), randomY(), t.getX(), t.getY()));
		}

		Clue c;

		for (int i = 0; i < maxTreasure; i++) {
			c = (Clue) randomArtifacts.get(i + maxTreasure);
			randomArtifacts.add(new Clue(faker.color().name(), randomX(), randomY(), c.getX(), c.getY()));
		}

		artifacts = new HashSet<Artifact>(randomArtifacts);
		
	}

}
