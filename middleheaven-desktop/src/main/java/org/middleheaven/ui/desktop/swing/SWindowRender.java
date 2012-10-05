package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

/**
 * Swing window {@link UIRender}.
 */
public class SWindowRender extends SwingUIRender {


	private static final long serialVersionUID = 4677259014974279595L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,
			UIComponent component) {
		
		return new SWindow();
	}

}
