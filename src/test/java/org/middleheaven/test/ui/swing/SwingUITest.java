package org.middleheaven.test.ui.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.JFrame;

import org.junit.Test;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.XMLUIComponentBuilder;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.swing.SwingRenderKit;


public class SwingUITest {

	@Test
	public void testXMLBuilder(){
		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder();
		UIComponent root = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/test/ui/swing/ui.xml"));
		assertNotNull(root);
		assertEquals(1,root.getChildrenCount());
		assertEquals(2,root.getChildrenComponents().iterator().next().getChildrenCount());
	
	}
	@Test
	public void testSwingRenderKit(){
		
		SwingRenderKit srk = new SwingRenderKit();
		
		RenderingContext context = new RenderingContext();
		UIEnvironment uie = new UIEnvironment();
		
		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder();
		UIComponent root = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/test/ui/swing/ui.xml"));

		UIComponent wui = srk.renderComponent(context, uie, root);
		
		assertNotNull(wui);
		assertEquals(root.getGID(), wui.getGID());
		assertTrue(wui instanceof JFrame);
		
	}
}
