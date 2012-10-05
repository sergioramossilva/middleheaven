/**
 * 
 */
package org.middleheaven.process.web;

import java.io.Writer;

import org.middleheaven.io.repository.ContentSource;
import org.middleheaven.io.repository.MediaManagedFileContent;

/**
 * 
 */
public interface HttpEntry extends ContentSource {

	public String getContentEncoding();
	
	public boolean isRepeatable();
	
	public boolean isStreaming();
	
	public boolean isChunked();
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public MediaManagedFileContent getContent();

	/**
	 * @return
	 */
	public Writer getContentWriter();

}
