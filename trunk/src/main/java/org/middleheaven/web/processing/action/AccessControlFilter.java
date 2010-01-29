package org.middleheaven.web.processing.action;

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
import org.middleheaven.aas.UserAgent;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.util.StringUtils;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.HttpFilter;
import org.middleheaven.web.processing.HttpFilterChain;
import org.middleheaven.web.processing.HttpUrl;
import org.middleheaven.web.processing.Outcome;
import org.middleheaven.web.processing.RedirectAfterCookie;
import org.middleheaven.web.processing.RequestCookie;

public class AccessControlFilter implements HttpFilter{

	private AccessControlService accessControlService;
	private List<URLPermission> permissions = new LinkedList<URLPermission>();
	private Outcome failureOutcome;
	private Outcome loginOutcome;
	private Outcome accessDeniedOutcome;
	private SignatureStore<HttpContext> store;

	public AccessControlFilter(
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

	private void letPass(HttpContext context,HttpFilterChain chain, AccessRequest request){
		context.setAttribute(ContextScope.REQUEST, AccessRequest.class.getName(), 	request);
		chain.doChain(context);
	}
	@Override
	public void doFilter(HttpContext context, HttpFilterChain chain) {

		Permission[] permissions = getGuardPermission(context.getRequestUrl());
		final CallbacksSet set = new CallbacksSet();
		
		CallbackHandler handler = new CallbackHandler(){

			@Override
			public CallbacksSet getCallbacks() {
				return set;
			}

			@Override
			public void onException(AccessException e) {
				e.printStackTrace(); //TODO log
			}

		};
		
		HttpContextAccessRequest request = new HttpContextAccessRequest(context,handler,store);

		if (permissions.length == 0){
			// free for all
			letPass(context,chain, request);
		} else {
			
			boolean repeat=true;
			loop:while (repeat){
				
				final AccessRequestBroker accessRequestBroker = accessControlService.accessRequestBroker();
				LoginStep step = accessRequestBroker.broke(request);
				switch (step){
				case FAIL:
					chain.interruptWithOutcome(failureOutcome);
					break loop;
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
					RequestCookie redirectCookie =	context.getAttribute(ContextScope.REQUEST_COOKIES, "redirect_after_login", RequestCookie.class);
					if (redirectCookie != null){ // read redirect cookie
						RedirectAfterCookie rc = new RedirectAfterCookie(redirectCookie);
						chain.interruptWithOutcome(rc.asOutcome());
						context.setAttribute(ContextScope.REQUEST_COOKIES, "redirect_after_login", rc.expire());
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
						context.setAttribute(ContextScope.REQUEST, "authentication.callbackset", set);
						chain.interruptWithOutcome(loginOutcome);
						
						// memorize target page
						RedirectAfterCookie rc = new RedirectAfterCookie("redirect_after_login", context.getRequestUrl().toString());
						context.setAttribute(ContextScope.REQUEST_COOKIES, "redirect_after_login", rc);
						break;
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
				nc.setName(context.getAttribute(ContextScope.PARAMETERS,nc.getPrompt(), String.class));
			} else if (callback instanceof PasswordCallback){
				PasswordCallback pc = ((PasswordCallback) callback);
				String attribute = context.getAttribute(ContextScope.PARAMETERS, pc.getPrompt(), String.class);
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
