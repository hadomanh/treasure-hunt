package ngakinz.search;

import java.util.List;

import ngakinz.player.Player;

public interface SearchAlgorithm {
	
	public Player search(List<Player> players, String name);

}
