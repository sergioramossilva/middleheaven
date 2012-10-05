package org.middleheaven.ui;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.ui.AbstractUIContainerModel;
import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.testui.TestRenderKit;
import org.middleheaven.ui.testui.TestUIComponent;



public class UITest extends MiddleHeavenTestCase{
	
	@Test
	public void testRendering(){
		
		GenericUIComponent root =  (GenericUIComponent)GenericUIComponent.getInstance(UIClient.class, null);
		UIComponent frame = root.addComponent(UIView.class, null);
		frame.setUIModel(new AbstractUIContainerModel());
		
		TestRenderKit rk = new TestRenderKit();
		RenderingContext context = new RenderingContext(rk);
		
		UIComponent renderedUIC = rk.renderComponent(context,null, root);
		
		assertTrue (renderedUIC.isRendered());
		assertTrue (renderedUIC instanceof  TestUIComponent);
	}


}
