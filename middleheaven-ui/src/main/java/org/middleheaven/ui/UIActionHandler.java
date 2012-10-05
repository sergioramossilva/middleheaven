/**
 * 
 */
package org.middleheaven.ui;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.events.UIActionEvent;

/**
 * 
 */
public interface UIActionHandler {

	/**
	 * 
	 * @param event
	 */
	public void handleAction(UIActionEvent event, AttributeContext attributeContext);
}
