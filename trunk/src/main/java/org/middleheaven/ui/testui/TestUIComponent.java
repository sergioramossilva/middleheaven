package org.middleheaven.ui.testui;

import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.rendering.RenderType;

/**
 * This component can be used to test proposes.
 * Is offers no graphic interface thus being suitable for a off screen 
 * test. 
 * 
 */
public class TestUIComponent extends GenericUIComponent {


	private boolean hasFocus = false;
	
	public TestUIComponent(RenderType renderType, String familly) {
		super(renderType, familly);
	}

	@Override
	public void gainFocus() {
		hasFocus = true; // TODO remove focus from all others
	}

	@Override
	public boolean hasFocus() {
		return hasFocus;
	}
}
