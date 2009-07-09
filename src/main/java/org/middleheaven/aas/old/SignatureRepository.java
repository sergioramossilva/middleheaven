package org.middleheaven.aas.old;

import org.middleheaven.aas.Signature;

public interface SignatureRepository {

	public void remove(Signature signature);
	
	public void store(Signature signature);
	
	public Signature findByName(String name);
}
