/**
 * 
 */
package org.middleheaven.process.web;

import org.middleheaven.io.repository.MediaManagedFileContent;

/**
 * 
 */
public interface HttpEntry  {

	public String getContentEncoding();
	
	public boolean isRepeatable();
	
	public boolean isStreaming();
	
	public boolean isChunked();
	
	public MediaManagedFileContent getContent();

}
