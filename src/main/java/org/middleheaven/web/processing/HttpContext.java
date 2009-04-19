package org.middleheaven.web.processing;

import java.util.Map;

import org.middleheaven.global.Culture;
import org.middleheaven.io.repository.ManagedFileRepository;
import org.middleheaven.io.repository.MediaManagedFile;
import org.middleheaven.ui.CulturalAttributeContext;
import org.middleheaven.web.processing.action.HttpMethod;

public interface HttpContext extends CulturalAttributeContext{

	public ManagedFileRepository getUploadRepository();
	
	public Map<String,String> getParameters();
	
	public StringBuilder getRequestUrl();
	
	public  HttpMethod getHttpService();
	
	public  HttpUserAgent getAgent();
	
	public  MediaManagedFile responseMediaFile();

	public  Culture getCulture();

	// TODO redo model
	public String getContextPath();
}
