package org.middleheaven.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.models.UISelectionModel;
import org.middleheaven.ui.models.impl.ListSelectionModel;

public class TestGenericUIComponent {

	
	@Test
	public void testCreate(){
		
		UISelectionModel model = new ListSelectionModel();
		
		UIComponent uic = GenericUIComponent.getInstance(UISelectOne.class, "familly");
		uic.setUIModel(model);
		
		uic.setGID("id");
		
		assertTrue(uic instanceof UISelectOne);
		
		assertNotNull(uic.getUIModel());
		
		assertEquals("familly",uic.getFamily());
		assertEquals("id",uic.getGID());
	}
}
