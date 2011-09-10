package org.middleheaven.aas.old;

import org.middleheaven.aas.Role;

public class NameRole implements Role {

	private String name;
	
	public NameRole(){}
	
	public NameRole(String name){
		this.name =  name;
	}

	public String getName() {
		return name;
	}


}
