package ngakinz.player;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ngakinz.artifact.Artifact;
import ngakinz.artifact.Treasure;

@Data
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
	
	public void move(int toX, int toY) {
		double distance = Math.sqrt(Math.pow(toX - x, 2) + Math.pow(toY - y, 2));
		
		if (distance <= speed) {
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
		sb.append(name + " (" + x + "," + y + ") (" + xDest + "," + yDest + ") ");

		for (Artifact a : collection) {
			sb.append(a.getId() + " ");
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

