package org.middleheaven.process.web.client;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpUrl;


/**
 * 
 */
public interface HttpClientRequest  {

	/**
	 * @return
	 */
	HttpUrl getRequestUrl();

	/**
	 * @return
	 */
	AttributeContext getAttributes();


	HttpEntry getEntry();

	/**
	 * @return
	 */
	HttpMethod getMethod();
}
