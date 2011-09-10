package org.middleheaven.aas;

import java.net.InetAddress;



public abstract class AccessRequest {

	public abstract InetAddress getCallerAddress();

	public abstract CallbackHandler getCallbackHandler();
	public abstract Subject getSubject();
	
	protected abstract void setSubject(Subject subject);
	
	public abstract Signature getSignature();
	public abstract void setSignature(Signature signature);
}
