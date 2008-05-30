package org.middleheaven.aas;

public class NameRole implements Role {

	private String name;
	
	public NameRole(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return NamedPermittion.forName(name).implies(permission);
	}
}
