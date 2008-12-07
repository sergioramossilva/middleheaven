package org.middleheaven.aas;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Represents an application user (human or otherwise)
 * 
 * Authentication is the process of attributing roles based on credencials the user has;
 * 
 *
 */
public abstract class User implements Serializable , ResourceAcessor{


	private static final long serialVersionUID = 242268313443167436L;
	
	protected Set<Credential> publicCredentials = new CopyOnWriteArraySet<Credential>();
	protected Set<Role> roles = new CopyOnWriteArraySet<Role>();
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * user is signed if it has a <code>SignatureCredential</code>
	 * and the credential is current
	 * @return
	 */
	public final boolean isSigned(){
		
		 for (Credential credential : this.publicCredentials){
			 if (SignatureCredential.class.isInstance(credential)){
				 SignatureCredential sc = (SignatureCredential)credential;
				 return sc.isCurrent();
			 }
		 }
		 return false;
	}
	
	/**
	 * user is identified if its name is not null
	 * @return <code>true</code> if the name is not null, <code>false</code> otherwise
	 */
	public final boolean isIdentified(){
		return this.name != null;
	}
	
	/**
	 * User is autenticated if it has any role.
	 * @return <code>true</code> if the user has any role, <code>false</code> otherwise
	 */
	public final boolean isAutenticated(){
		return !this.roles.isEmpty();
	}
	
	public final boolean hasPermission(Permission permission){
		for (Role role : roles){
			if (role.hasPermission(permission)){
				return true;
			}
		}
		return false;
	}
	
	protected Set<Role> getRoles(){
		return Collections.unmodifiableSet(roles);
	}
	
	protected  void addRole(Role role){
		roles.add(role);
	}
	
	protected  void removeRole(Role role){
		roles.remove(role);
	}
	
	public final boolean isInRole(String name){
		for (Role role : roles){
			if (role.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	public boolean hasCredential(Credential credential) {
		return publicCredentials.contains(credential);
	}

	public Set<Credential> getPublicCredentials(){
		return Collections.unmodifiableSet(publicCredentials);
	}
	
	public <C extends Credential> Set<C> getPublicCredentials(Class<C> credentialType){
		 Set<C> result = new HashSet<C>();
		
		 for (Credential credential : this.publicCredentials){
			 if (credentialType.isInstance(credential)){
				 result.add(credentialType.cast(credential));
			 }
		 }
		 
		 return result;
	}

}
