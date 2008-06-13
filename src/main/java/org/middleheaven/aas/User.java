package org.middleheaven.aas;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.core.services.ServiceRegistry;

/**
 * Represents an application user (human or otherwise)
 * 
 *
 */
public abstract class User implements Serializable {

	private Map<String , Role> roles = new TreeMap<String,Role>();
	private boolean autenticated;
	private boolean identified;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public final boolean isSigned(){
		SignatureService ss = ServiceRegistry.getService(SignatureService.class); 
		return ss.isSigned(this);
	}
	
	public final boolean isIdentified(){
		return this.identified;
	}
	
	public final boolean isAutenticated(){
		return this.autenticated;
	}
	
	public final boolean hasPermission(Permission permission){
		for (Role r : roles.values()){
			if (r.hasPermission(permission)){
				return true;
			}
		}
		return false;
	}
	
	public final boolean isInRole(String name){
		return roles.containsKey(name);
	}
	
	final void setAutenticated (boolean autenticated){
		this.autenticated = autenticated;
	}
	
	final void setIdentified (boolean identified){
		this.identified = identified;
	}
	
	final void addRole(Role role){
		this.roles.put(role.getName(), role);
	}
	
	final void removeRole(Role role){
		this.roles.remove(role.getName());
	}
}
