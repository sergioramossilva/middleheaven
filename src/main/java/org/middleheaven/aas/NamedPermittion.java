package org.middleheaven.aas;

public class NamedPermittion implements Permission {

	
	private String name;

	public static NamedPermittion forName(String name){
		NamedPermittion np = new NamedPermittion();
		np.name = name;
		return np;
	}
	
	public NamedPermittion(){}

	@Override
	public boolean implies(Permission other) {
		if (other instanceof NamedPermittion){
			this.name.equals(((NamedPermittion)other).name);
		}
		return false;
	}
}
