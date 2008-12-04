package org.middleheaven.aas;

public interface RefreshableCredential extends Credential{

	/**
	 * Expands the validity of this credential.
	 * An <code>RefreshFailedException</code> is throwned if it is not possivle to further
	 * expand the validity 
	 */
	public void refresh() throws RefreshFailedException;
	
	/**
	 * Determines if the credential is currently valid. 
	 * @return <code>true</code> is it is currently valid, <code>false</code> otherwise
	 */
	public boolean isCurrent();
}
