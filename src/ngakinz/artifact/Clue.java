package ngakinz.artifact;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Clue extends Artifact {
	
	private int xDest, yDest;
	private boolean visited = false;

	public Clue(String id, int x, int y, int xDest, int yDest) {
		super(id, x, y);
		this.xDest = xDest;
		this.yDest = yDest;
	}

	public Clue(Clue clue, int x, int y) {
		super(clue.getId(), x, y);
		this.xDest = clue.xDest;
		this.yDest = clue.yDest;
	}

	@Override
	public Artifact collect(int x, int y) {
		return new Clue(this, x, y);
	}

}

