package org.middleheaven.aas;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.aas.old.SignaturePolicy;


public class AccessRequestBroker {

	Autorizator autorizator = new Autorizator();
	Autenticator autenticator = new Autenticator();
	SubjectLocator subjectLocator;
	SignaturePolicy policy;

	public AccessRequestBroker(){}
	
	public LoginStep broke(AccessRequest request){
		CallbackHandler callbackHandler = request.getCallbackHandler();

		try{
			Signature signature = request.getSignature();

			if (signature == null || !signature.isValid() || signature.getCredentials().isEmpty()){
				// autentication
				Set<Credential> credentials = new HashSet<Credential>();

				CallbacksSet callbackSet = callbackHandler.getCallbacks();

				if(callbackSet.isEmpty()){
					autenticator.registerCallbacks(callbackSet);
					return LoginStep.HANDLE_CALLBACK;
				} else if (callbackSet.isBlank()){
					return LoginStep.HANDLE_CALLBACK;
				}

				try {
					autenticator.autenticate(credentials, callbackSet);
				
					// autorize
					autorize (credentials, request);

					return LoginStep.SUCESS;
					
				} catch (FailureAutenticationException e){
					callbackHandler.onException(e);
					return LoginStep.FAIL;
				} catch (AuthenticationException e) {
					callbackHandler.onException(e);
					return LoginStep.FAIL;
				}

			} else {
				signature.refresh();

				// autorization
				Set<Credential> credentials = signature.getCredentials();

				autorize (credentials, request);
				
				return LoginStep.SUCESS;
			}
		}catch (RuntimeException e){
			callbackHandler.onException(new AccessException(e));
			return LoginStep.FAIL;
		}
	}

	private void autorize(Set<Credential> credentials, AccessRequest request){
		Set<Role> roles = new HashSet<Role>();
		
		autorizator.autorize(credentials, roles);

		// load subject
		Subject subject = subjectLocator.load(credentials, roles);

		//create signature
		Signature signature = policy.createSignature(subject);
		
		//store signature
		request.setSignature(signature);

		// store subject
		request.setSubject(subject);

	}
}
