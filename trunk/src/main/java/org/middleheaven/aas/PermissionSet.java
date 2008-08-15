package org.middleheaven.aas;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.middleheaven.util.CollectionUtils;

public class PermissionSet implements Permission {

	Set<Permission> permissions = new HashSet<Permission>();
	
	@Override
	public boolean implies(Permission other) {
		// TODO Auto-generated method stub
		return false;
	}

	public PermissionSet add(Permission ... permission) {
		permissions.addAll(Arrays.asList(permission));
		return this;
	}

	public PermissionSet remove(Permission permission) {
		permissions.remove(permission);
		
		return this;
	}
	
	public boolean equals(Object other) {
		return other instanceof PermissionSet && equals((PermissionSet) other);
	}

	public boolean equals(PermissionSet other) {
		return CollectionUtils.equals(this.permissions, other.permissions);
	}

	public int hashCode() {
		return permissions.hashCode();
	}
}
