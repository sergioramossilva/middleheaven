/**
 * 
 */
package org.middleheaven.process.web;

import java.net.InetAddress;

/**
 * Aggregates all information about the communication being established.
 * 
 */
public interface HttpChannel {

	/**
	 * 1.0 / 1.1
	 */
	
	public String getProtocol();


	/**
	 * {@inheritDoc}
	 */
	
	public String getServerName();
	
	/**
	 * {@inheritDoc}
	 */
	
	public int getServerPort();


	/**
	 * {@inheritDoc}
	 */
	
	public String getRemoteAddr();

	/**
	 * {@inheritDoc}
	 */
	
	public String getRemoteHost();

	
	/**
	 * {@inheritDoc}
	 */
	
	public String getLocalAddr();

	/**
	 * {@inheritDoc}
	 */
	
	public int getLocalPort();

	/**
	 * {@inheritDoc}
	 */
	
	public String getRemoteUser();

	/**
	 * {@inheritDoc}
	 */
	
	public String getAuthType();
	
	/**
	 * {@inheritDoc}
	 */
	
	public boolean isSecure();

	/**
	 * {@inheritDoc}
	 */
	
	public int getRemotePort();

	/**
	 * {@inheritDoc}
	 */
	
	public String getLocalName();


	/**
	 * @return
	 */
	public InetAddress getRemoteAddress();

	
}
