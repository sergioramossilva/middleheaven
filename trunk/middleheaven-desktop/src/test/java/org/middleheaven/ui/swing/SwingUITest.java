package org.middleheaven.ui.swing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UITreeCriteria;
import org.middleheaven.ui.XMLUIComponentBuilder;
import org.middleheaven.ui.desktop.awt.Desktop;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;


public class SwingUITest extends MiddleHeavenTestCase {

	@Test
	public void testXMLBuilder(){
		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService());
		UIEnvironment root = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/swing/ui.xml"));
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

		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService());
		UIEnvironment env = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/swing/ui.xml"));

		UIClient client = env.getClients().iterator().next();
		
		
		assertNotNull("UIModel is null",client.getUIModel());
		
		RenderKit kit = client.getUIModel().getRenderKit();
		
		assertNotNull("RenderKit is null",kit);
		
		RenderingContext context = new RenderingContext(kit);
		
		UIClient rclient = kit.renderComponent(context, null, client);
		
		assertNotNull(rclient);
		assertTrue(rclient instanceof Desktop);
		assertEquals(client.getGID(), rclient.getGID());
		
		UIComponent wui = rclient.getChildrenComponents().get(0);
		assertTrue("Window is not a JFrame", wui instanceof JFrame);
		
		
		UIComponent layoutui = wui.getChildrenComponents().get(0);
		assertNotNull(layoutui);
		assertTrue("Window children is not a JPanel", layoutui instanceof JPanel);
		
	}
	
	@Test
	public void textUIQuery(){

		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService());
		UIEnvironment env = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/swing/ui.xml"));

		UIClient client = env.getClients().iterator().next();
		
		assertNotNull("UIModel is null",client.getUIModel());
		
		RenderKit kit = client.getUIModel().getRenderKit();
		RenderingContext context = new RenderingContext(kit);
		
		client = kit.renderComponent(context, null, client);
		
		UIComponent wui = client.getChildrenComponents().get(0);
	
		UIComponent layoutui = wui.getChildrenComponents().get(0);
		assertNotNull(layoutui);
	
		List<UIComponent> components = UITreeCriteria.search("/").execute(layoutui)
		.list();
		
		assertFalse(components.isEmpty());
		assertEquals(client, components.get(0));
		
		components = UITreeCriteria.search("..").execute(layoutui)
		.list();
		
		assertFalse(components.isEmpty());
		assertEquals(wui, components.get(0));
		
		components = UITreeCriteria.search(".").execute(layoutui)
		.list();
		
		assertFalse(components.isEmpty());
		assertEquals(layoutui, components.get(0));
		
		
		components = UITreeCriteria.search("./../..").execute(layoutui)
		.list();
		
		assertFalse(components.isEmpty());
		assertEquals(client, components.get(0));
		
		
		components = UITreeCriteria.search("frameA").execute(layoutui)
		.list();
		
		UIComponent frameA = components.get(0);
		assertFalse(components.isEmpty());
		assertEquals("frameA", frameA.getGID());
		
		components = UITreeCriteria.search("frameB").execute(layoutui)
		.list();
		
		assertFalse(components.isEmpty());
		
		UIComponent frameB = components.get(0);
		assertEquals("frameB", frameB.getGID());
	}
}