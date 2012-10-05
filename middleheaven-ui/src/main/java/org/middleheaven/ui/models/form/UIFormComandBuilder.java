/**
 * 
 */
package org.middleheaven.ui.models.form;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.MethodUIActionHandler;
import org.middleheaven.ui.NamingContainer;
import org.middleheaven.ui.UIActionHandler;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UITreeCriteria;
import org.middleheaven.ui.events.UIActionEvent;

/**
 * 
 */
public class UIFormComandBuilder {

	private AbstractSheetSetUIFormModel abstractSheetSetUIFormModel;
	private String name;

	/**
	 * Constructor.
	 * @param name 
	 * @param abstractSheetSetUIFormModel
	 */
	UIFormComandBuilder(String name, AbstractSheetSetUIFormModel abstractSheetSetUIFormModel) {
		this.abstractSheetSetUIFormModel = abstractSheetSetUIFormModel;
		this.name = name;
	}

	/**
	 * 
	 */
	public void handleWith(UIActionHandler handler) {
		this.abstractSheetSetUIFormModel.handlersByName.put(name, handler);
	}
	
	public void handleWithMethod(Object instance, String methodName) {
		this.handleWith(MethodUIActionHandler.newInstance(instance, methodName));
	}

	/**
	 * @param string the GID of the window to show
	 */
	public void navigateTo(final String name) {
		this.handleWith(new UIActionHandler() {
			
			@Override
			public void handleAction(UIActionEvent event, AttributeContext attributeContext) {
			
				UIClient client = (UIClient) UITreeCriteria.search("/").execute(event.getSource()).first();
				
				UIComponent nextWindow = ((NamingContainer)client).findContainedComponent(name);
				
				client.getUIModel().getSceneNavigator().show(nextWindow);
			}
		});
	}

}
