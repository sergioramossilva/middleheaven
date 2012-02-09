/**
 * 
 */
package org.middleheaven.process.web;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 */
public class HttpServletRequestHttpChannel implements HttpChannel {

	
	private ServletRequest request;

	public HttpServletRequestHttpChannel (ServletRequest request){
		this.request = request;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProtocol() {
		return request.getProtocol();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServerName() {
		return request.getServerName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getServerPort() {
		return request.getServerPort();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalAddr() {
		return request.getLocalAddr();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLocalPort() {
		return request.getLocalPort();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRemoteUser() {
		if (this.request instanceof HttpServletRequest) {
			return ((HttpServletRequest) request).getRemoteUser();
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthType() {
		if (this.request instanceof HttpServletRequest) {
			return ((HttpServletRequest) request).getAuthType();
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSecure() {
		return request.isSecure();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRemotePort() {
		return this.request.getRemotePort();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocalName() {
		return this.request.getLocalName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InetAddress getRemoteAddress() {
		try {
			return  InetAddress.getByName(this.request.getRemoteAddr());
		} catch (UnknownHostException e) {
			return null;
		}
	}

}
