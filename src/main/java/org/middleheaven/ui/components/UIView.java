package org.middleheaven.ui.components;

import org.middleheaven.ui.UIPrespective;
import org.middleheaven.ui.models.UIViewModel;


public interface UIView extends UIPrespective {

	public UIViewModel getUIModel();
}
