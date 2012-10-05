package org.middleheaven.aas;

import java.util.Set;


/**
 * An {@link AuthenticationModule} that use a simple name-password pair callback.
 * 
 */
public class PasswordAutenticationModule implements AuthenticationModule {

	private PasswordVerifier verifier;

	
	private String name = "authentication.name";
	private String password = "authentication.password";
	
	/**
	 * 
	 * Constructor.
	 * @param verifier
	 */
	public PasswordAutenticationModule(PasswordVerifier verifier){
		this.verifier = verifier;
	}
	
	/**
	 * 
	 * Constructor.
	 * @param verifier
	 * @param name the string identifier to use for the "name" callback
	 * @param password the string identifier to use for the "password" callback
	 */
	public PasswordAutenticationModule(PasswordVerifier verifier, String name, String password){
		this.verifier = verifier;
		this.name = name;
		this.password = password;
	}

	@Override
	public void registerCallbacks(CallbacksSet callbackSet) {
		callbackSet.add(new NameCallback(name,""));
		callbackSet.add(new PasswordCallback(password));
	}
	
	@Override
	public void autenticate(Set<Credential> credentials,
			CallbacksSet callbackSet) throws AuthenticationException {
		

		
		String name = callbackSet.getCallback(NameCallback.class).getName();
		char[] password = callbackSet.getCallback(PasswordCallback.class).getPassword();
		
		try{
			verifier.verify (name, password);
			
			credentials.add(new NameCredential(name));
		} catch (InvalidPasswordException e){
			// do not add credential
			throw new FailureAutenticationException();
		}
	}





}
