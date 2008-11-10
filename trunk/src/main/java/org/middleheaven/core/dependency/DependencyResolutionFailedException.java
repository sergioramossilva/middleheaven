package org.middleheaven.core.dependency;

import java.util.List;

public class DependencyResolutionFailedException extends RuntimeException {

	List<?> failedDependencies;
	
	public DependencyResolutionFailedException(List<?> failedDependencies) {
		super("Not all dependencies could be resolve");
		this.failedDependencies = failedDependencies;
	}

	public List<?> getFailedDependencies() {
		return failedDependencies;
	}
	
	

}
