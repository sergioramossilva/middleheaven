package org.middleheaven.aas;

import java.util.Set;

public class TimedSignature implements Signature {

	private Set<Credential>  credentials;
	private int timeOut;
	private long timeStamp;
	
	public TimedSignature(Set<Credential>  credentials, int timeOut){
		this.credentials  = credentials;
		this.timeOut = timeOut;
		this.timeStamp = System.currentTimeMillis();
	}
	
	@Override
	public Set<Credential> getCredentials() {
		return credentials;
	}
	
	@Override
	public boolean isValid() {
		return System.currentTimeMillis() - this.timeStamp < this.timeOut;
	}

	@Override
	public void refresh() {
		this.timeStamp = System.currentTimeMillis();
	}

}
