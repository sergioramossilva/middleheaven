package org.middleheaven.ui;

import org.junit.Test;
import org.middleheaven.tool.test.MiddleHeavenTestCase;

/**
 * 
 */
public class XmlUI2Test extends MiddleHeavenTestCase {

	final UIEnvironmentType uiType = UIEnvironmentType.TEST;
	

	
	@Test
	public void testXMLBuilder(){
	
//		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService(),uiType);
//		UIEnvironment env = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/ui.xml"));
//
//		assertNotNull(env);
//
//		assertNotNull(env.getClient());
//		// one window
//		assertEquals(1,env.getClient().getChildrenCount());
//		// 2 views
//		assertEquals(1,env.getClient().getChildrenComponents().size());
	}
	
	
	@Test
	public void textUIQuery(){

//		XMLUIComponentBuilder xmlBuilder = new XMLUIComponentBuilder(getWiringService(),uiType);
//		UIEnvironment env = xmlBuilder.buildFrom(new File("./src/test/java/org/middleheaven/ui/ui.xml"));
//
//
//		UIClient client = env.getClient();
//		
//		UIComponent wui = client.getChildrenComponents().get(0);
//	
//		UIComponent layoutui = wui.getChildrenComponents().get(0);
//		assertNotNull(layoutui);
//	
//		List<UIComponent> components = UITreeCriteria.search("/").execute(layoutui)
//		.list();
//		
//		assertFalse(components.isEmpty());
//		assertEquals(client, components.get(0));
//		
//		components = UITreeCriteria.search("..").execute(layoutui)
//		.list();
//		
//		assertFalse(components.isEmpty());
//		assertEquals(wui, components.get(0));
//		
//		components = UITreeCriteria.search(".").execute(layoutui)
//		.list();
//		
//		assertFalse(components.isEmpty());
//		assertEquals(layoutui, components.get(0));
//		
//		
//		components = UITreeCriteria.search("./../..").execute(layoutui)
//		.list();
//		
//		assertFalse(components.isEmpty());
//		assertEquals(client, components.get(0));
//		
//		
//		components = UITreeCriteria.search("frameA").execute(layoutui)
//		.list();
//		
//		UIComponent frameA = components.get(0);
//		assertFalse(components.isEmpty());
//		assertEquals("frameA", frameA.getGID());
//		
//		components = UITreeCriteria.search("frameB").execute(layoutui)
//		.list();
//		
//		assertFalse(components.isEmpty());
//		
//		UIComponent frameB = components.get(0);
//		assertEquals("frameB", frameB.getGID());
	}

}
