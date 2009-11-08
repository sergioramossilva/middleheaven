package org.middleheaven.aas;

import org.middleheaven.crypto.Base64CipherAlgorithm;
import org.middleheaven.ui.ContextScope;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.RequestCookie;

public class HttpContextCookiesSignatureStore extends HttpContextSignatureStore {

	private final int sessionTimeout; 

	public HttpContextCookiesSignatureStore() {
		this(60*30); // 30 minutes
	}
	
	public HttpContextCookiesSignatureStore(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	
	@Override
	public Signature getSignature(HttpContext context) {
		RequestCookie cookie = context.getAttribute(ContextScope.REQUEST_COOKIES, "mh_user_signature", RequestCookie.class);
		
		return null;
	}

	@Override
	public void setSignature(HttpContext context, Signature signature) {
		RequestCookie cookie= new RequestCookie("mh_user_signature", "");
		cookie.expire();
		if (signature.isValid()){
			String name=null;
			for (Credential c : signature.getCredentials()){
				if (c instanceof NameCredential){
					name = ((NameCredential)c).getName();
					break;
				}
			}
			
			if (name != null){
				// the user is identified
				String ticket = name + "@" + Integer.toHexString(name.hashCode() * 37);
				String value = new String(new Base64CipherAlgorithm().cipher(ticket.getBytes()));
				cookie = new RequestCookie("mh_user_signature", value);
				cookie.setMaxAge(sessionTimeout);
			}
		} 
		context.setAttribute(ContextScope.REQUEST_COOKIES, "mh_user_signature", cookie);
	}



}
