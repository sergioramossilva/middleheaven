/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;

import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public interface HTMLDocumentWritable {
	
	public void writeTo(HtmlDocument doc,RenderingContext context) throws IOException;

}
