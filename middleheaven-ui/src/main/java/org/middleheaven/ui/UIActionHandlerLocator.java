/**
 * 
 */
package org.middleheaven.ui;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.events.UIActionEvent;

/**
 * 
 */
public class UIActionHandlerLocator {
	
	
	private String commandName;
	private AttributeContext attributeContext;

	
	public static UIActionHandlerLocator getLocator(AttributeContext attributeContext){
		return new UIActionHandlerLocator(attributeContext);
	}
	/**
	 * Constructor.
	 * @param commandName
	 */
	private UIActionHandlerLocator(AttributeContext attributeContext) {
		this.attributeContext = attributeContext;
	}

	public UIActionHandlerLocator handle (String commandName){
		this.commandName = commandName;
		return this;
	}
	
	public void from(UIComponent component){
		bubbleEvent(component, new UIActionEvent(commandName, component));
	}
	
	private void bubbleEvent(UIComponent component, UIActionEvent event){
		
		if (component.getUIModel() instanceof UIActionHandler) {
			
			
			((UIActionHandler) component.getUIModel()).handleAction(event, attributeContext );
		} else if (component.getUIParent() == null){
			// reached the top. TODO use default handler
			
			throw new UnsupportedOperationException("Action " + event.getName() + " from " + event.getSource().getGID() + " was not handled.");
		
		} else {
			bubbleEvent (component.getUIParent(), event);
		}
	}

}
