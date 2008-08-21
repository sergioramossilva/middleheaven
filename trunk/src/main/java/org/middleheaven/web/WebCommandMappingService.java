package org.middleheaven.web;

public interface WebCommandMappingService {

	public WebCommandMapping resolve(CharSequence url);

	public void addInterceptor ( Interceptor  interceptor , CharSequence url);
}
