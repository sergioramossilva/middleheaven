package org.middleheaven.aas;

import java.net.InetAddress;



public abstract class AccessRequest {

	public abstract UserAgent getAgent();
	public abstract InetAddress getCallerAddress();
	public abstract SignatureStore getSignatureStore();
	public abstract CallbackHandler getCallbackHandler();
	public abstract Subject getSubject();
	
	protected abstract void setSubject(Subject subject);
}
