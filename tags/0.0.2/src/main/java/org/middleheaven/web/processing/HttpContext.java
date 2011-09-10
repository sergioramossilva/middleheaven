package org.middleheaven.web.processing;

import java.net.InetAddress;
import java.util.Map;

import org.middleheaven.global.Culture;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.ui.CulturalAttributeContext;
import org.middleheaven.web.processing.action.HttpMethod;

/**
 * A context that is scoped inside an HTTP request.
 */
public interface HttpContext extends CulturalAttributeContext{

	public ManagedFileRepository getUploadRepository();
	
	public Map<String,String> getParameters();
	
	public HttpUrl getRequestUrl();
	
	/**
	 * 
	 * @return the origin of the request.
	 */
	public HttpUrl getRefererUrl();
	
	public  HttpMethod getHttpService();
	
	public  HttpUserAgent getAgent();
	
	public  MediaManagedFile responseMediaFile();

	public  Culture getCulture();

	// TODO redo model
	public String getContextPath();

	/**
	 * 
	 * @return the address of the remote caller or null if its not available
	 */
	public InetAddress getRemoteAddress();
}
