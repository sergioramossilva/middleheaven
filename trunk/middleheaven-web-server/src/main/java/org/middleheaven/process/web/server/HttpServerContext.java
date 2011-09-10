package org.middleheaven.process.web.server;

import java.net.InetAddress;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaManagedFile;

/**
 * A context that is scoped inside an HTTP request.
 */
public interface HttpServerContext  {

	/**
	 * 
	 * @return the request received at the server.
	 */
	public HttpServerRequest getRequest();
	
	/**
	 * 
	 * @return The server response.
	 */
	public HttpServerResponse getResponse();
	
	
	/**
	 * Access the upload repository for files uploaded in this request.
	 * 
	 * @return the upload repository.
	 */
	public ManagedFileRepository getUploadRepository();
	
	/**
	 * 
	 * @return
	 */
	public String getContextPath();

	/**
	 * 
	 * @return the address of the remote caller or null if its not available
	 */
	public InetAddress getRemoteAddress();
}
