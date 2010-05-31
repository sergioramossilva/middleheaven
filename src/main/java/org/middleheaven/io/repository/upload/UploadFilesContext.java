package org.middleheaven.io.repository.upload;

import java.util.Map;

import org.middleheaven.io.repository.ManagedFileRepository;

public class UploadFilesContext {

	private ManagedFileRepository repository;
	private Map<String, String> parameters;

	
	public UploadFilesContext(ManagedFileRepository repository,
			Map<String, String> parameters) {
		super();
		this.repository = repository;
		this.parameters = parameters;
	}

	public ManagedFileRepository getManagedFileRepository(){
		return repository;
	}

	public Map<String, String> getParametersMap(){
		return parameters;
	}

}
