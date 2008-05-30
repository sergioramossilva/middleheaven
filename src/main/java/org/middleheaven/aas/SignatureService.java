package org.middleheaven.aas;

public interface SignatureService {

	
	public boolean isSigned(User user);
	public void keepSigned(User user);
}
