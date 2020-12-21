package ngakinz.sort;

import java.util.List;

import ngakinz.player.Player;

public class QuickSortAlgorithm implements SortAlgorithm {

	public int partition(List<Player> players, int low, int high) {
		Player pivot = players.get(high);
		int i = (low - 1); // index of smaller element
		for (int j = low; j < high; j++) {
			// If current element is bigger than the pivot
			if (players.get(j).getTreasure() > pivot.getTreasure()) {
				i++;

				// swap arr[i] and arr[j]
				Player temp = players.get(i);
				players.set(i, players.get(j));
				players.set(j, temp);
			}
		}

		// swap arr[i+1] and arr[high] (or pivot)
		Player temp = players.get(i + 1);
		players.set(i + 1, players.get(high));
		players.set(high, temp);

		return i + 1;
	}

	public void sort(List<Player> players, int low, int high) {
		if (low < high) {
			/*
			 * pi is partitioning index, arr[pi] is now at right place
			 */
			int pi = partition(players, low, high);

			// Recursively sort elements before
			// partition and after partition
			sort(players, low, pi - 1);
			sort(players, pi + 1, high);
		}
	}

	@Override
	public void sort(List<Player> players) {
		sort(players, 0, players.size() - 1);
	}

}
