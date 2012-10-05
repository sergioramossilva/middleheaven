package org.middleheaven.aas;

import org.middleheaven.util.Hash;


public final class RolePermission implements Permission {


	private String name;
	private boolean linient = false;

	public static RolePermission of(String name){
		RolePermission np = new RolePermission();
		np.name = name;
		return np;
	}

	/**
	 * Allows any role
	 * @return
	 */
	public static RolePermission any(){
		RolePermission np = new RolePermission();
		np.linient = true;
		return np;
	}

	private RolePermission(){}

	@Override
	public boolean implies(Permission other) {
		if (other instanceof RolePermission){
			return this.name.equals(((RolePermission)other).name);
		}
		return false;
	}

	@Override
	public boolean isLenient() {
		return linient;
	}

	@Override
	public boolean isStrict() {
		return false;
	}

	public String getRoleName(){
		return name;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		return "RolePermission[" + this.getRoleName() + "]";
	}

	public int hashCode(){
		return Hash.hash(this.name).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof RolePermission) && equalsRolePermission((RolePermission)obj); 
	}


	private boolean equalsRolePermission(RolePermission other) {
		return (this.name != null ? this.name.equals(other.name) : other.name == null) && this.linient == other.linient;
	}
}
