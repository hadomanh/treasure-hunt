package ngakinz.search;

import java.util.List;

import ngakinz.player.Player;

public class SearchEngine {
	
	private static final SearchAlgorithm searchAlgorithm = new BinarySearchAlgorithm();
	
	public static Player search(List<Player> players, String name) {
		return searchAlgorithm.search(players, name);
	}

}
