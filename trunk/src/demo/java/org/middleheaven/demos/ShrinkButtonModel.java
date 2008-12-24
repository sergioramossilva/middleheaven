package org.middleheaven.demos;

import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UITreeCriteria;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.models.AbstractUICommandModel;

public class ShrinkButtonModel extends AbstractUICommandModel {

	@Override
	public void onCommand(UICommand command) {
		List<UIComponent> res  = UITreeCriteria.search("../buttonA").execute(command);
		
		UIComponent grow = res.get(0);
		grow.getUIModel().setEnabled(true);
	}



}
