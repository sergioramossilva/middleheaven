package org.middleheaven.aas;

public interface PermissionResolver {

	
	public boolean hasPermission(Subject s , Permission p);
}
