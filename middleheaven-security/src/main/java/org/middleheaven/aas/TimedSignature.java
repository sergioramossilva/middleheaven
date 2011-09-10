package org.middleheaven.aas;

import java.util.Set;

import org.middleheaven.quantity.time.Period;

public class TimedSignature implements Signature {

	private Set<? extends Credential>  credentials;
	private Period timeOut;
	private long timeStamp;
	
	/**
	 * 
	 * @param credentials
	 * @param timeOut in seconds
	 */
	public TimedSignature(Set<? extends Credential>  credentials, Period timeOut){
		this.credentials  = credentials;
		this.timeOut = timeOut;
		this.timeStamp = System.currentTimeMillis();
	}
	
	@Override
	public Set<Credential> getCredentials() {
		return (Set<Credential>) credentials;
	}
	
	@Override
	public boolean isValid() {
		return timeOut == null || ( System.currentTimeMillis() - this.timeStamp < this.timeOut.milliseconds());
	}

	@Override
	public void refresh() {
		this.timeStamp = System.currentTimeMillis();
	}

}
