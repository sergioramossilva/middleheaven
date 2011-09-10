/**
 * 
 */
package org.middleheaven.web.container;

import org.middleheaven.process.web.HttpServerContainerName;
import org.middleheaven.util.Version;

/**
 * 
 */
public class WebContainerInfo {

	public static final String ATTRIBUTE_NAME = "_webContainerInfo";
	
	private HttpServerContainerName name;
	private Version version;
	
	/**
	 * 
	 * Constructor.
	 * @param name container name.
	 * @param version container version.
	 */
	public WebContainerInfo (HttpServerContainerName name, Version version){
		this.name = name;
		this.version = version;
	}
	/**
	 * 
	 * @return the container name
	 */
	public HttpServerContainerName getName() {
		return name;
	}
	
	/**
	 * 
	 * @return the container version. 
	 */
	public Version getVersion(){
		return version;
	}
}
