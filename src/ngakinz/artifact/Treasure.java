package ngakinz.artifact;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Treasure extends Artifact {
	
	private int instances;
	
	private String desc;

	public Treasure(String id, int x, int y, int instances, String desc) {
		super(id, x, y);
		this.instances = instances;
		this.desc = desc;
	}

	@Override
	public Artifact collect(int x, int y) {
		if (instances <= 0) {
			return null;
		}
		return new Treasure(this.getId(), x, y, --instances, desc);
	}

}

