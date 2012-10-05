/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.util.List;

import org.middleheaven.ui.UIActionHandler;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UISearch;
import org.middleheaven.ui.UISearch.UISearchFilter;
import org.middleheaven.ui.UITreeCriteria;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.ui.models.UICommandModel;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

/**
 * 
 */
public class VaadinButton extends VaadinUIComponent implements UICommand{

	
	
	private transient UIForm form;
	
	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinButton(Component component) {
		super(component, UICommand.class);
	}

	public UICommandModel getUIModel(){
		return (UICommandModel) super.getUIModel();
	}

	
	protected UIForm getForm (){
		
		if (form == null) {
			
			form = (UIForm) UISearch.searchFirstUp(this, new UISearchFilter() {
				
				@Override
				public boolean canContinueSearch(UIComponent c) {
					return true;
				}
				
				@Override
				public boolean accept(UIComponent c) {
					return c instanceof UIForm;
				}
			});
		}
		
		return form;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		

		Button component = (Button) this.getComponent();
		final UICommandModel uiModel = this.getUIModel();
		
		if (uiModel.getText() != null){
			component.setCaption(this.localize(uiModel.getText()));
		}
		
		if (uiModel.getHandlers() == null || uiModel.getHandlers().isEmpty()){
			
			for (UICommandModel commandModel  : getForm().getUIModel().getActions()){
				
				if (commandModel.getName().equals(uiModel.getName())){
					this.setUIModel(commandModel);
					break;
				}
			}
		}
			
		
		component.addListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				
		
					UIActionEvent uiEvent = new UIActionEvent( getUIModel().getName(), VaadinButton.this);
					
					for (UIActionHandler handler : getUIModel().getHandlers()){
					
						handler.handleAction(uiEvent, getAttributeContext());
					}
			
				
			}
			
		});
	}

}
