package org.middleheaven.demos;

import java.util.Set;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIQuery;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.models.AbstractUICommandModel;

public class ShrinkButtonModel extends AbstractUICommandModel {

	@Override
	public void onCommand(UICommand command) {
		Set<UIComponent> res  = command.findComponents(UIQuery.find("//"));
		
		UIComponent grow = res.iterator().next();
		grow.getUIModel().setEnabled(false);
	}



}
