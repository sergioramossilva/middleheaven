package org.middleheaven.test.ui.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.JFrame;

import org.junit.Test;
import org.middleheaven.ui.MapContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.XMLUIComponentBuilder;
import org.middleheaven.ui.desktop.swing.SwingRenderKit;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;


public class SwingUITest {

	@Test
	public void testXMLBuilder(){
		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder();
		UIEnvironment root = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/test/ui/swing/ui.xml"));
		assertNotNull(root);
		// one client
		assertEquals(1,root.getClients().size());
		// one window
		assertEquals(1,root.getClients().iterator().next().getChildrenCount());
		// 2 views
		assertEquals(1,root.getClients().iterator().next().getChildrenComponents().size());
	}
	
	
	@Test
	public void testSwingRenderKit(){

		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder();
		UIEnvironment env = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/test/ui/swing/ui.xml"));

		UIClient client = env.getClients().iterator().next();
		
		assertNotNull("UIModel is null",client.getUIModel());
		
		RenderKit kit = client.getUIModel().getRenderKit();
		
		RenderingContext context = new RenderingContext(kit);
		
		UIClient rclient = kit.renderComponent(context, client, null);
		
		assertNotNull(rclient);
		assertEquals(client.getGID(), rclient.getGID());
		
		UIComponent wui = rclient.getChildrenComponents().get(0);
		assertTrue(wui instanceof JFrame);
		
	}
}
