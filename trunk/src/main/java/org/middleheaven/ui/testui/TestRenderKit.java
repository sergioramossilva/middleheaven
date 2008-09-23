package org.middleheaven.ui.testui;

import org.middleheaven.ui.UIArea;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.RenderType;
import org.middleheaven.ui.rendering.UIRender;
import org.middleheaven.ui.rendering.UIUnitConverter;

public class TestRenderKit extends AbstractRenderKit {


	private static final long serialVersionUID = 925791236014175947L;
	
	final TestUIRender render = new TestUIRender();
	final UIUnitConverter converter = new UIUnitConverter(){

		@Override
		protected double[] getDialogBaseUnits(UIArea layoutable) {
			return new double[]{100.0,100.0};
		}
		
	};
	
	@Override
	protected boolean isRendered(UIComponent component) {
		return component.isRendered();
	}


	@Override
	public UIUnitConverter getUnitConverted() {
		return converter;
	}

	public UIRender getRender(RenderType componentType, String familly) {
		return render;
	}


	@Override
	public void show(UIComponent component) {
		component.setVisible(true);
	}


	@Override
	public void dispose(UIComponent splash) {
		//no-op
	}
	        
}
