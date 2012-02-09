package org.middleheaven.process.web.server.filters;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.middleheaven.aas.AccessControlService;
import org.middleheaven.aas.AccessException;
import org.middleheaven.aas.AccessRequest;
import org.middleheaven.aas.AccessRequestBroker;
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
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.HttpCookie;
import org.middleheaven.process.web.HttpUrl;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.HttpServerSignatureStorePolicy;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.RedirectAfterCookie;
import org.middleheaven.util.StringUtils;

public class AccessControlFilter implements HttpFilter{

	private AccessControlService accessControlService;
	private List<URLPermission> permissions = new LinkedList<URLPermission>();
	private Outcome failureOutcome;
	private Outcome loginOutcome;
	private Outcome accessDeniedOutcome;
	private HttpServerSignatureStorePolicy storePolicy;

	public AccessControlFilter(
			AccessControlService accessControlService,
			HttpServerSignatureStorePolicy storePolicy, 
			Outcome loginOutcome,
			Outcome failureOutcome,
			Outcome accessDeniedOutcome){
		
		this.storePolicy = storePolicy;
		this.accessControlService = accessControlService;
		this.failureOutcome = failureOutcome;
		this.loginOutcome = loginOutcome;
	}

	private void letPass(HttpServerContext context,HttpFilterChain chain, AccessRequest request){
		context.getAttributes().setAttribute(ContextScope.REQUEST, AccessRequest.class.getName(), 	request);
		chain.doChain(context);
	}
	@Override
	public void doFilter(HttpServerContext context, HttpFilterChain chain) {

		Permission[] permissions = getGuardPermission(context.getRequestUrl());
		final CallbacksSet set = new CallbacksSet();
		
		CallbackHandler handler = new CallbackHandler(){

			@Override
			public CallbacksSet getCallbacks() {
				return set;
			}

			@Override
			public void onException(AccessException e) {
				
			}

		};
		
		HttpContextAccessRequest request = new HttpContextAccessRequest(context,handler,storePolicy.resolveSignatureStore(context));

		final AccessRequestBroker accessRequestBroker = accessControlService.accessRequestBroker();
		LoginStep step = accessRequestBroker.broke(request);
		
		if (permissions.length == 0){
			// free for all
			letPass(context,chain, request);
		} else {
			
			boolean repeat=true;
			do{
				step = accessRequestBroker.broke(request);
				
				switch (step){
				case FAIL:
					chain.interruptWithOutcome(failureOutcome);
					repeat=false;
					break;
				case SUCCESS:
					// is authenticated
					
					Subject subject = request.getSubject();

					// assert permissions
					for (Permission p : permissions){
						if (!accessRequestBroker.hasPermission(subject, p)){
							chain.interruptWithOutcome(accessDeniedOutcome);
							return;
						}
					}
					
					// all fine. go on
					repeat=false;
					HttpCookie redirectCookie =	context.getAttributes().getAttribute(ContextScope.REQUEST_COOKIES, "redirect_after_login", HttpCookie.class);
					if (redirectCookie != null){ // read redirect cookie
						RedirectAfterCookie rc = new RedirectAfterCookie(redirectCookie);
						chain.interruptWithOutcome(rc.asOutcome());
						context.getAttributes().setAttribute(ContextScope.REQUEST_COOKIES, "redirect_after_login", rc.expire());
					} else {
					
						letPass(context,chain, request);
					}
					break;
				case HANDLE_CALLBACK:

					// try to fullfill the callbacks
					fillCallbacks(set,context);
				
					// if was not possible, invoke login page
					if (set.isBlank()){
						repeat = false;
						context.getAttributes().setAttribute(ContextScope.REQUEST, "authentication.callbackset", set);
						chain.interruptWithOutcome(loginOutcome);
						
						// memorize target page
						RedirectAfterCookie rc = new RedirectAfterCookie("redirect_after_login", context.getRequestUrl().toString());
						context.getAttributes().setAttribute(ContextScope.REQUEST_COOKIES, "redirect_after_login", rc);
						break;
					} 
				}
			} while (repeat);
		}

	}
	
	private void fillCallbacks(CallbacksSet set , HttpServerContext context){
		AttributeContext attributes = context.getAttributes();
		
		for (Callback callback : set){
			if (callback instanceof IPAddressCallback){
				((IPAddressCallback) callback).setAddress(context.getRemoteAddress());
			} else if (callback instanceof NameCallback){
				NameCallback nc = ((NameCallback) callback);
				nc.setName(attributes.getAttribute(ContextScope.PARAMETERS,nc.getPrompt(), String.class));
			} else if (callback instanceof PasswordCallback){
				PasswordCallback pc = ((PasswordCallback) callback);
				String attribute = attributes.getAttribute(ContextScope.PARAMETERS, pc.getPrompt(), String.class);
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

	private Permission[] getGuardPermission(HttpUrl url) {
		for (URLPermission up : this.permissions){
			if (up.match(url.toString())){
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


		private HttpServerContext context;
		private CallbackHandler handler;
		private SignatureStore store;

		public HttpContextAccessRequest(HttpServerContext context,CallbackHandler handler, SignatureStore store){
			this.context = context;
			this.store = store;
			this.handler = handler;
		}

		@Override
		public CallbackHandler getCallbackHandler() {
			return handler;
		}

		@Override
		public InetAddress getCallerAddress() {
			return context.getHttpChannel().getRemoteAddress();
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
			return store.getSignature();
		}

		@Override
		public void setSignature(Signature signature) {
			 store.setSignature(signature);
		}

	}



}
