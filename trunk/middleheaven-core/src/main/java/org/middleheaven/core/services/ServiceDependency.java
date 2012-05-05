/**
 * 
 */
package org.middleheaven.core.services;

/**
 * 
 */
public class ServiceDependency {

	
	public static ServiceDependency required (Service necessaryService){
		return new ServiceDependency(necessaryService, true);
	}
	
	public static ServiceDependency optional (Service necessaryService){
		return new ServiceDependency(necessaryService, false);
	}

	private boolean required;
	private Service necessaryService;
	
	public ServiceDependency (Service necessaryService, boolean required){
		this.required = required;
		this.necessaryService = necessaryService;
	}
}
