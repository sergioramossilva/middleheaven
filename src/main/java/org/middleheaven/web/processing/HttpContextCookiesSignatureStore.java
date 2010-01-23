package org.middleheaven.web.processing;

import java.util.Collections;

import org.middleheaven.aas.Credential;
import org.middleheaven.aas.NameCredential;
import org.middleheaven.aas.Signature;
import org.middleheaven.aas.TimedSignature;
import org.middleheaven.crypto.Base64CipherAlgorithm;
import org.middleheaven.quantity.time.Period;
import org.middleheaven.ui.ContextScope;

public class HttpContextCookiesSignatureStore extends HttpContextSignatureStore {

	private final Period sessionTimeout; 

	public HttpContextCookiesSignatureStore() {
		this(Period.minutes(30));
	}
	
	public HttpContextCookiesSignatureStore(Period sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	
	@Override
	public Signature getSignature(HttpContext context) {
		RequestCookie cookie = context.getAttribute(ContextScope.REQUEST_COOKIES, "mh_user_signature", RequestCookie.class);
		
		if (cookie !=null){
			String ticket = new String(new Base64CipherAlgorithm().revertCipher(cookie.getValue().getBytes()));
			
			String name = ticket.split("@")[0];
			String hash = ticket.split("@")[1];
			if (hash.equals(Integer.toHexString(name.hashCode() * 37))){
				return new TimedSignature(Collections.singleton(new NameCredential(name)), cookie.getMaxAge());
			}
		}
		
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
				cookie.setDomain(context.getRequestUrl().getHost());
				cookie.setPath(context.getContextPath());
				cookie.setMaxAge(sessionTimeout);
			}
		} 
		context.setAttribute(ContextScope.REQUEST_COOKIES, "mh_user_signature", cookie);
	}



}
