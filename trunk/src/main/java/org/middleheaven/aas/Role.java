package org.middleheaven.aas;

import java.io.Serializable;

/**
 * Represents a role of the user in the use of the application.
 * Roles can be mapped to actors who use the application. 
 *
 */
public interface Role extends Serializable{


	public String getName();
	public boolean hasPermission(Permission permission);
	
}
