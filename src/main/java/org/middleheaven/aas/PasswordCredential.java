package org.middleheaven.aas;

public interface PasswordCredential extends ExpirableCredential{

	public byte[] getPassword();
	
}
