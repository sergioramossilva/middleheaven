package org.middleheaven.aas;


public final class RolePermission implements Permission {

	
	private String name;

	public static RolePermission of(String name){
		RolePermission np = new RolePermission();
		np.name = name;
		return np;
	}
	
	private RolePermission(){}

	@Override
	public boolean implies(Permission other) {
		if (other instanceof RolePermission){
			this.name.equals(((RolePermission)other).name);
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
	
	public String getRoleName(){
		return name;
	}
}
