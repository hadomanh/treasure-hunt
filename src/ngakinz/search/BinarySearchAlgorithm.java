package ngakinz.search;

import java.util.List;

import ngakinz.player.Player;

public class BinarySearchAlgorithm implements SearchAlgorithm {

	Player search(List<Player> players, int l, int r, String name) {
		if (r >= l) {
			int mid = l + (r - l) / 2;

			// If the element is present at the
			// middle itself
			if (players.get(mid).getName().equalsIgnoreCase(name))
				return players.get(mid);

			// If element is smaller than mid, then
			// it can only be present in left subarray
			if (players.get(mid).getName().compareTo(name) < 0)
				return search(players, l, mid - 1, name);

			// Else the element can only be present
			// in right subarray
			return search(players, mid + 1, r, name);
		}

		// We reach here when element is not present
		// in array
		return null;
	}

	@Override
	public Player search(List<Player> players, String name) {
		return search(players, 0, players.size()-1, name);
	}

}
