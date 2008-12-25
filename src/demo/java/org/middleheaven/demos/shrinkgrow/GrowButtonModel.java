package org.middleheaven.demos.shrinkgrow;

import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UITreeCriteria;
import org.middleheaven.ui.UIUtils;
import org.middleheaven.ui.models.AbstractUICommandModel;
import org.middleheaven.ui.models.UIActionEvent;

public class GrowButtonModel extends AbstractUICommandModel {


	@Override
	public void onCommand(UIActionEvent event) {
		List<UIComponent> res  = UITreeCriteria.search("/window").execute(event.getSource());
		
		UIComponent window = res.get(0);
		UIComponent client = window.getUIParent();
		
		window.setSize(new UIDimension(client.getDimension().getWidth(),client.getDimension().getHeight()));
		
		UIUtils.center(window);
	}



}
