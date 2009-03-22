package org.middleheaven.web.processing.action;

import java.util.Map;

import org.middleheaven.global.Culture;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.ui.AbstractContext;
import org.middleheaven.web.processing.HttpUserAgent;



public abstract class WebContext extends AbstractContext{


	public static final String UPLOADS_FILESYSTEM = "_UPLOADS_FILESYSTEM";
	public static final String APPLICATION_CONTEXT_FILESYSTEM = "_APPLICATION_CONTEXT_FILESYSTEM";
	public static final String REQUEST_REMOTE_ADDRESS = "_REQUEST_REMOTE_ADDRESS";
	public static final String REQUEST_REMOTE_HOST = "_REQUEST_REMOTE_HOST";
	public static final String REQUEST_SERVER_PORT = "_REQUEST_SERVER_PORT";
	public static final String REQUEST_SERVER_NAME = "_REQUEST_SERVER_NAME";
	public static final String REQUEST_URL = "_REQUEST_URL";

	public WebContext() {
		super();
	}
	
	public abstract HttpMethod getHttpService();
	
	public abstract HttpUserAgent getAgent();
	
	public abstract MediaManagedFile responseMediaFile();

	protected abstract Map<String,String> getParameters();
	
	public abstract Culture getCulture();
}
