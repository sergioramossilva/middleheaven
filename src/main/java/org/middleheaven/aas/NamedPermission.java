package org.middleheaven.aas;

public final class NamedPermission implements Permission {

	
	private String name;

	public static NamedPermission forName(String name){
		NamedPermission np = new NamedPermission();
		np.name = name;
		return np;
	}
	
	public NamedPermission(){}

	@Override
	public boolean implies(Permission other) {
		if (other instanceof NamedPermission){
			this.name.equals(((NamedPermission)other).name);
		}
		return false;
	}

	@Override
	public boolean isLenient() {
		return false;
	}

	@Override
	public boolean isStrict() {
		return false;
	}
}
