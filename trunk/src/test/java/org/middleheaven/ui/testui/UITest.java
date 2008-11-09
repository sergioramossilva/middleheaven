package org.middleheaven.ui.testui;
import static org.junit.Assert.*;

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
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderType;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.testui.TestRenderKit;
import org.middleheaven.ui.testui.TestUIComponent;



public class UITest {

	private static GenericUIComponent root;
	@BeforeClass
	public static void setUp(){
		Container container = new DesktopUIContainer(ManagedFileRepositories.resolveFile(new File(".")));
		StandaloneBootstrap bootstrap = new StandaloneBootstrap(container);
		bootstrap.start(new ConsoleLogBook(LoggingLevel.ALL));
		root = new GenericUIComponent(RenderType.ROOT, null);
		UIComponent frame = root.addComponent(RenderType.FRAME, null);
		frame.setUIModel(new AbstractUIContainerModel());
	
	}
	
	@Test
	public void testRendering(){
		TestRenderKit rk = new TestRenderKit();
		RenderingContext context = new RenderingContext();
		
		UIComponent renderedUIC = rk.renderComponent(context, null, root);
		
		assertTrue (renderedUIC.isRendered());
		assertTrue (renderedUIC instanceof  TestUIComponent);
	}
}
