package org.middleheaven.aas;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.quantity.time.Period;

/**
 * Determines the next LoginStep based on the current state of the request.
 */
public final class AccessRequestBroker {

	private Autorizator autorizator = new Autorizator();
	private Authenticator autenticator = new Authenticator();
	private SubjectLocator subjectLocator = new DefaultSubjectLocator();
	private SignaturePolicy policy = new TemporarySignaturePolicy(Period.seconds(30));
	private PermissionResolver permissionResolver = new RolePermissionResolver();
	
	public AccessRequestBroker(){}
	

	public void addAuthenticationModule(AuthenticationModule module, SuccessLevel level) {
		autenticator.addAuthenticationModule(module, level);
	}


	public void addAutorizationModule(AutorizationModule module) {
		autorizator.addAutorizationModule(module);
	}
	
	public void setSubjectLocator(SubjectLocator subjectLocator ){
		this.subjectLocator = subjectLocator;
	}
	
	public void setSignaturePolicy(SignaturePolicy policy ){
		this.policy = policy;
	}
	
	public void setPermissionResolver (PermissionResolver permissionResolver){
		this.permissionResolver = permissionResolver;
	}
	
	
	public boolean hasPermission (Subject subject, Permission p){
		return permissionResolver.hasPermission(subject, p);
	}
	
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

					return LoginStep.SUCCESS;
					
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
				
				return LoginStep.SUCCESS;
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
		Signature signature = policy.createSignature(credentials);
		
		//store signature
		request.setSignature(signature);

		// store subject
		request.setSubject(subject);

	}
}
