package org.middleheaven.aas;

import java.util.Set;



public class PasswordAutenticationModule implements AuthenticationModule {

	private PasswordVerifier verifier;

	public PasswordAutenticationModule(PasswordVerifier verifier){
		this.verifier = verifier;
	}

	@Override
	public void registerCallbacks(CallbacksSet callbackSet) {
		callbackSet.add(new NameCallback("authentication.name",""));
		callbackSet.add(new PasswordCallback("authentication.password"));
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
