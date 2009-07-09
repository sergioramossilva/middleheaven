package org.middleheaven.aas;

import java.io.Serializable;
import java.util.Set;


public interface Signature extends Serializable {

	boolean isValid();
	
	public Set<Credential> getCredentials();
	
	void refresh();

}
