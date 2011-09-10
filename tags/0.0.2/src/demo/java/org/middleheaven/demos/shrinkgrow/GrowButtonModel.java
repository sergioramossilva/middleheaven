package org.middleheaven.demos.shrinkgrow;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UITreeCriteria;
import org.middleheaven.ui.UIUtils;
import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.ui.models.AbstractUICommandModel;

public class GrowButtonModel extends AbstractUICommandModel {


	@Override
	public void onCommand(UIActionEvent event) {

		UIComponent window = UITreeCriteria.search("/window")
							.execute(event.getSource())
							.first(UIComponent.class);
							
		UIComponent client = window.getUIParent();
		
		window.setSize(new UIDimension(client.getDimension().getWidth(),client.getDimension().getHeight()));
		
		UIUtils.center(window);
	}



}
