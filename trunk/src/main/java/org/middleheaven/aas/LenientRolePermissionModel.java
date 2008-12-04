package org.middleheaven.aas;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * Implementation of RolesPermissionModel where 
 * all roles have all permissions
 */
public class LenientRolePermissionModel implements RolesPermissionModel {

	private static final LenientPermissionSet permit = new LenientPermissionSet();
	
	@Override
	public PermissionSet getRolePermissions(Role role) {
		return permit;
	}


	private static class LenientPermissionSet extends PermissionSet {

		private static final long serialVersionUID = 1L;

		public LenientPermissionSet(){

		}

		public void add(ResourcePermission p){

		}

		public int size(){
			return 1;
		}

		public boolean equals(Object other){
			return other instanceof LenientPermissionSet;
		}

		public int hashCode(){
			return 0;
		}

		public boolean implies(Permission threshold) {
			return true;
		}

		Iterator<ResourcePermission> iterator(){
			Set<ResourcePermission> all =  Collections.emptySet();
			return all.iterator();
		}

		public boolean isLenient() {
			return false;
		}

		public String toString (){
			return "LenientPermission";

		}

		public boolean isStrict() {
			return false;
		}


	}
}
