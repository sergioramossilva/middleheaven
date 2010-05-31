package org.middleheaven.ui.web.html.tags;


import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.models.UISelectionModel;
import org.middleheaven.ui.web.HtmlSelectionModel;
import org.middleheaven.ui.web.tags.TagContext;

public class UISelectOneTag extends AbstractUIComponentIterationTagSupport  {


	private UISelectionModel model;
	private Object selected;
	
	public void setModel(UISelectionModel model ){
		this.model = model;
	}

	public void setSelectedItemValue(Object selected){
		this.selected = selected;
	}
	
	@Override
	public UIModel getModel() {
		return model;
	}
	
	public void releaseState(){
		super.releaseState();
		this.model = null;
		this.selected = null;
	}

	protected boolean beforeStart() {
		if(this.model!=null){
			return false; // do not iterate
		}
		this.model = new HtmlSelectionModel();
		return true;
	}
	
	@Override
	public Class<? extends UIComponent> getComponentType() {
		return UISelectOne.class;
	}
	
	protected void prepareRender(TagContext attributeContext) {
		super.prepareRender(attributeContext);
		
		model.setName(this.getName());

		if(selected!=null && model.isSelectionEmpty()){
			
			int index = model.indexOf(selected);
			
			if(index  < 0 ){
				model.setSelectionInterval(0, 0);
			} else {
				model.setSelectionInterval(index, index);
			}
			
		}
	}
	
	public void addElement(String caption , Object value){
		
		((HtmlSelectionModel)model).addElement(caption, value.toString(), false);
		
	}

	




}
