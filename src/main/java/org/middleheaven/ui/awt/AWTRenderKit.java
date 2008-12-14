package org.middleheaven.ui.awt;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.UIUnitConverter;

public class AWTRenderKit extends AbstractRenderKit {

	private static final long serialVersionUID = -6998801713950007439L;

	@Override
	public void dispose(UIComponent splash) {
		// TODO implement RenderKit.dispose
		
	}

	@Override
	public UIUnitConverter getUnitConverted() {
		// TODO implement RenderKit.getUnitConverted
		return null;
	}

	@Override
	public void show(UIComponent component) {
		component.setVisible(true);
	}


}
