package ngakinz.sort;

import java.util.List;

import ngakinz.player.Player;

public class SortEngine {
	
	private static final SortAlgorithm sortAlgorithm = new QuickSortAlgorithm();
	
	public static void sort(List<Player> players) {
		sortAlgorithm.sort(players);
	}

}
