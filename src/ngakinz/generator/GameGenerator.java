package ngakinz.generator;

import java.util.List;
import java.util.Set;

import ngakinz.artifact.Artifact;
import ngakinz.player.Player;

public interface GameGenerator {
	
	Set<Artifact> getArtifacts();
	
	List<Player> getPlayers();
	
	Player getPlayer();

}
