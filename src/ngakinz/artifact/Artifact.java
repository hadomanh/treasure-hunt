package ngakinz.artifact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
public abstract class Artifact {
	
	private String id;
	
	private int x, y;

	public boolean isHere(int x, int y) {
		return this.x == x && this.y == y;
	}
	
	public boolean isSameId(Artifact artifact) {
		return this.id == artifact.id;
	}
	
	public abstract Artifact collect(int x, int y);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artifact other = (Artifact) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	

}

