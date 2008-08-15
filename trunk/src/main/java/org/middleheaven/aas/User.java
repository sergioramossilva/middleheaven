package org.middleheaven.aas;

import java.io.Serializable;

import org.middleheaven.core.services.ServiceRegistry;

/**
 * Represents an application user (human or otherwise)
 * 
 *
 */
public abstract class User implements Serializable {

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
	
	public final boolean isInRole(String roleName){
		return AuthenticationContext.getAuthenticationContext().getUserRolesModel().isUserInRole(this, roleName);
	}
	
	final void setAutenticated (boolean autenticated){
		this.autenticated = autenticated;
	}
	
	final void setIdentified (boolean identified){
		this.identified = identified;
	}
	
}
