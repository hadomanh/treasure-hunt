package ngakinz.player;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import ngakinz.artifact.Artifact;
import ngakinz.artifact.Clue;
import ngakinz.artifact.Treasure;

@Data
@NoArgsConstructor
public abstract class Player {

	private String name;

	private int speed, x, y, xDest, yDest;

	public List<Artifact> collection = new ArrayList<Artifact>();

	public Player(String name, int speed, int x, int y, int xDest, int yDest) {
		super();
		this.name = name;
		this.speed = speed;
		this.x = x;
		this.y = y;
		this.xDest = xDest;
		this.yDest = yDest;
		this.collection.add(new Clue("INIT", x, y, xDest, yDest));
	}

	public boolean inSameLocation(Artifact a) {
		return x == a.getX() && y == a.getY();
	}
	
	public boolean inSameLocation(Player p) {
		return x == p.x && y == p.y;
	}

	public void addArtifact(Artifact a) {
			this.collection.add(a);
			if (a.getClass() == Treasure.class) {
				System.out.println("** " + name + " collected a treasure: " + ((Treasure)a).getDesc());
			} else {
				System.out.println("** " + name + " collected a clue");
			}
			
	}
	
	public abstract void giveCluesToPerson(Player p);

	public boolean update() {
		return (x != xDest || y != yDest);

	}
	
	public boolean isPossible(int toX, int toY) {
		
		double distance = Math.sqrt(Math.pow(toX - x, 2) + Math.pow(toY - y, 2));
		
		return distance <= speed;
		
	}
	
	public void move(int toX, int toY) {
		
		if (isPossible(toX, toY)) {
			this.x = toX;
			this.y = toY;
		}
		
	}

	public void move() {
		
		if (!update()) {
			return;
		}

		double min = Double.MAX_VALUE;
		int toX = 0, toY = 0;
		double distance;

		for (int i = x - speed; i <= x + speed; i++) {
			for (int j = y - speed; j <= y + speed; j++) {
				distance = Math.sqrt(Math.pow(xDest - i, 2) + Math.pow(yDest - j, 2));
				if (distance < min) {
					min = distance;
					toX = i;
					toY = j;
				}
			}
		}

		x = toX;
		y = toY;

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Character: " + name + "\n");
		sb.append("Type: " + this.getClass().getSimpleName() + "\n");
		sb.append("Speed: " + speed + "\n");
		sb.append("Location: (" + x + "," + y + ")\n");
		
		if (collection != null) {
			sb.append("Hint: ");
			for (Artifact a : collection) {
				if (a.getClass() == Clue.class) {
					sb.append(" (" + ((Clue)a).getXDest() + "," + ((Clue)a).getYDest() + ")");
				}
				
			}
			sb.append("\n");
			sb.append("Treasure:");
			
			for (Artifact a : collection) {
				if (a.getClass() == Treasure.class) {
					sb.append("\n\t" + ((Treasure)a).getDesc());
				}
				
			}
		}
		
		return sb.toString();
	}
	
	public int getTreasure() {
		
		int count = 0;
		
		for (Artifact a : collection) {
			if (a.getClass() == Treasure.class) {
				count++;
			}
		}
		
		return count;
	}

}

