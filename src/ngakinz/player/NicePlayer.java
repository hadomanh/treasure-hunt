package ngakinz.player;

import lombok.NoArgsConstructor;
import ngakinz.artifact.Artifact;
import ngakinz.artifact.Clue;

@NoArgsConstructor
public class NicePlayer extends Player {

	public NicePlayer(String name, int speed, int x, int y, int xDest, int yDest) {
		super(name, speed, x, y, xDest, yDest);
	}

	@Override
	public void giveCluesToPerson(Player p) {
		for (Artifact a : collection) {
			if (a.getClass() == Clue.class && !p.collection.contains(a)) {
				Clue aa = new Clue((Clue)a, a.getX(), a.getY());
				aa.setVisited(false);
				p.collection.add(aa);
				System.out.println("** " + this.getName() + " shared clues with " + p.getName());
			}
		}
		
	}

}