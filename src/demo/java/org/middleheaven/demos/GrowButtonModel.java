package org.middleheaven.demos;

import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.models.AbstractUICommandModel;

public class GrowButtonModel extends AbstractUICommandModel {

	@Override
	public void onCommand(UICommand command) {
		this.setEnabled(false);
	}



}
