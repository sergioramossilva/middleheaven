package org.middleheaven.aas;

import java.io.Serializable;

public interface Role extends Serializable{


	public String getName();
	public boolean hasPermission(Permission permission);
	
}
