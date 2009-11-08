package org.middleheaven.web.processing.action;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.middleheaven.aas.AccessControlService;
import org.middleheaven.aas.AccessException;
import org.middleheaven.aas.AccessRequest;
import org.middleheaven.aas.Callback;
import org.middleheaven.aas.CallbackHandler;
import org.middleheaven.aas.CallbacksSet;
import org.middleheaven.aas.IPAddressCallback;
import org.middleheaven.aas.LoginStep;
import org.middleheaven.aas.NameCallback;
import org.middleheaven.aas.PasswordCallback;
import org.middleheaven.aas.Permission;
import org.middleheaven.aas.Signature;
import org.middleheaven.aas.SignatureStore;
import org.middleheaven.aas.Subject;
import org.middleheaven.aas.UserAgent;
import org.middleheaven.ui.AttributeContext;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.util.StringUtils;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.Outcome;

public class AccessControlInterceptor implements Interceptor{

	private AccessControlService accessControlService;
	private List<URLPermission> permissions = new LinkedList<URLPermission>();
	private Outcome failureOutcome;
	private Outcome loginOutcome;
	private Outcome accessDeniedOutcome;
	private SignatureStore<HttpContext> store;

	public AccessControlInterceptor(
			AccessControlService accessControlService,
			SignatureStore<HttpContext> store, 
			Outcome loginOutcome,
			Outcome failureOutcome,
			Outcome accessDeniedOutcome){
		
		this.store = store;
		this.accessControlService = accessControlService;
		this.failureOutcome = failureOutcome;
		this.loginOutcome = loginOutcome;
	}

	@Override
	public void intercept(HttpContext context, InterceptorChain chain) {

		Permission[] permissions = getGuardPermission(context.getRequestUrl().toString());

		if (permissions.length == 0){
			// let pass
			chain.doChain(context);
		} else {
			final CallbacksSet set = new CallbacksSet();

			CallbackHandler handler = new CallbackHandler(){

				@Override
				public CallbacksSet getCallbacks() {
					return set;
				}

				@Override
				public void onException(AccessException e) {
					// TODO handle
				}

			};

			
			HttpContextAccessRequest request = new HttpContextAccessRequest(context,handler,store);

			boolean repeat=true;
			while (repeat){
				
				LoginStep step = accessControlService.accessRequestBroker().broke(request);
				switch (step){
				case FAIL:
					chain.interruptWithOutcome(failureOutcome);
					break;
				case SUCESS:
					// is authenticated
					
					Subject subject = request.getSubject();

					// assert permissions
					for (Permission p : permissions){
						if (!subject.hasPermission(p)){
							chain.interruptWithOutcome(accessDeniedOutcome);
							return;
						}
					}
					
					// all fine. go on
					repeat=false;
					chain.doChain(context);
					break;
				case HANDLE_CALLBACK:

					// try to fullfill the callbacks
					fillCallbacks(set,context);
				
					// if was not possible, invoke login page
					if (set.isBlank()){
						repeat = false;
						context.setAttribute(ContextScope.REQUEST, "authentication.callbackset", set);
						chain.interruptWithOutcome(loginOutcome);
					}
				}
			}
		}

	}
	
	private void fillCallbacks(CallbacksSet set , HttpContext context){
		for (Callback callback : set){
			if (callback instanceof IPAddressCallback){
				((IPAddressCallback) callback).setAddress(context.getRemoteAddress());
			} else if (callback instanceof NameCallback){
				NameCallback nc = ((NameCallback) callback);
				nc.setName(context.getAttribute(nc.getPrompt(), String.class));
			} else if (callback instanceof NameCallback){
				PasswordCallback pc = ((PasswordCallback) callback);
				String attribute = context.getAttribute(pc.getPrompt(), String.class);
				if (attribute==null){
					attribute = "";
				}
				pc.setPassword(attribute.toCharArray());
			}
		}
	}

	public void restrict(String urlPattern, Permission ... permissions){
		this.permissions.add(new URLPermission(urlPattern,permissions));
	}

	private Permission[] getGuardPermission(String url) {
		for (URLPermission up : this.permissions){
			if (up.match(url)){
				return up.permissions;
			}
		}
		return new Permission[0];
	}

	private static class URLPermission {

		private Permission[] permissions;
		private Pattern urlPattern;

		public URLPermission(String urlPattern, Permission ... permissions) {
			this.urlPattern = StringUtils.compile(urlPattern);
			this.permissions = permissions;
		}

		public boolean match (String url){
			return urlPattern.matcher(url).find();
		}

	}

	private static class HttpContextAccessRequest extends AccessRequest{


		private HttpContext context;
		private CallbackHandler handler;
		private SignatureStore<HttpContext> store;

		public HttpContextAccessRequest(HttpContext context,CallbackHandler handler, SignatureStore<HttpContext> store){
			this.context = context;
			this.store = store;
			this.handler = handler;
		}

		@Override
		public UserAgent getAgent() {
			return context.getAgent();
		}

		@Override
		public CallbackHandler getCallbackHandler() {
			return handler;
		}

		@Override
		public InetAddress getCallerAddress() {
			return context.getRemoteAddress();
		}

		Subject subject;
		
		@Override
		public Subject getSubject() {
			return subject;
		}

		@Override
		protected void setSubject(Subject subject) {
			this.subject = subject;
		}

		@Override
		public Signature getSignature() {
			return store.getSignature(context);
		}

		@Override
		public void setSignature(Signature signature) {
			 store.setSignature(context, signature);
		}

	}

}
