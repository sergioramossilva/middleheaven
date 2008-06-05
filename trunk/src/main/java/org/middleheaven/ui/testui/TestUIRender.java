package org.middleheaven.ui.testui;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class TestUIRender extends UIRender {

	private static final long serialVersionUID = 1L;

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		TestUIComponent t=  new TestUIComponent(component.getType(), component.getFamily());
		
		t.setUIModel(component.getUIModel());
		t.setID(component.getID());
		t.setUIParent(parent);

		return t;
	}

}
