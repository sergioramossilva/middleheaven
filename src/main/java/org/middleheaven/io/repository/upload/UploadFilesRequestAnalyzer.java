package org.middleheaven.io.repository.upload;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.io.repository.EmptyFileRepository;
import org.middleheaven.io.repository.ManagedFileRepository;


public class UploadFilesRequestAnalyzer {

	
	public UploadFilesRequestAnalyzer (){}
	
	
	public static UploadFilesContext getContext(HttpServletRequest request){
		ManagedFileRepository uploadRepository = EmptyFileRepository.repository();	
		@SuppressWarnings("unchecked") Map<String, String > parameters = request.getParameterMap();

		if (ClassIntrospector.isInClasspath("org.apache.commons.fileupload.servlet.ServletFileUpload")){
			if (ServletFileUpload.isMultipartContent(request)){

				parameters = new HashMap<String,String>();
				
				uploadRepository = UploadManagedFileRepository.repositoryOf(request,parameters);

				
			}
		} 
	
		return new UploadFilesContext (uploadRepository,parameters);
		
	
		
	}
	
	


}
