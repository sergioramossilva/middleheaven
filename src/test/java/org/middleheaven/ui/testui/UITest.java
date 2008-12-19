package org.middleheaven.ui.testui;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;
import org.middleheaven.core.Container;
import org.middleheaven.core.bootstrap.StandaloneBootstrap;
import org.middleheaven.core.bootstrap.client.DesktopUIContainer;
import org.middleheaven.io.repository.ManagedFileRepositories;
import org.middleheaven.logging.ConsoleLogBook;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.ui.AbstractUIContainerModel;
import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.rendering.RenderingContext;



public class UITest {

	@BeforeClass
	public static void setUp(){
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		
	
	}
	
	@Test
	public void testRendering(){
		
		GenericUIComponent root = new GenericUIComponent(UIClient.class, null);
		UIComponent frame = root.addComponent(UIView.class, null);
		frame.setUIModel(new AbstractUIContainerModel());
		
		TestRenderKit rk = new TestRenderKit();
		RenderingContext context = new RenderingContext(rk);
		
		UIComponent renderedUIC = rk.renderComponent(context,null, root);
		
		assertTrue (renderedUIC.isRendered());
		assertTrue (renderedUIC instanceof  TestUIComponent);
	}
}
