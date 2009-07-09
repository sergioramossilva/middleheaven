package org.middleheaven.aas;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Set of {@code AutorizationModule} that 
 * attribute roles ( and thus permissions) based on the given credentials.
 * 
 */
public class Autorizator {
	
	private final Collection<AutorizationModule> modules = new LinkedHashSet<AutorizationModule>();

	public Autorizator addAutorizationModule (AutorizationModule module){
		this.modules.add(module);
		return this;
	}
	
	public void autorize(Set<Credential> credentials, Set<Role> roles) {
		if(credentials.isEmpty()){
			return;
		}
		
		for(AutorizationModule module : modules){
			module.autorize(credentials, roles);
		}
	}

}
