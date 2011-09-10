package org.middleheaven.aas;


public interface PasswordVerifier {

	public void verify(String name, char[] password) throws InvalidPasswordException;
	
}
