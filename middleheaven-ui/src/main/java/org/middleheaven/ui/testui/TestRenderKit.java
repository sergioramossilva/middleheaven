package org.middleheaven.ui.testui;

import org.middleheaven.ui.Displayable;
import org.middleheaven.ui.SceneNavigator;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.UIRender;
import org.middleheaven.ui.rendering.UIUnitConverter;

/**
 * Produces UICompoenents in a test environment.
 */
public class TestRenderKit extends AbstractRenderKit {


	private static final long serialVersionUID = 925791236014175947L;
	
	final TestUIRender render = new TestUIRender();
	
	final UIUnitConverter converter = new TestUIUnitConverter();
	
	private static class TestUIUnitConverter extends UIUnitConverter {

		private static final long serialVersionUID = 8234808357882607288L;

		@Override
		protected double[] getDialogBaseUnits(Displayable layoutable) {
			return new double[]{100.0,100.0};
		}
		
	}
	
	@Override
	protected boolean isRendered(UIComponent component) {
		return component.isRendered();
	}


	@Override
	public UIUnitConverter getUnitConverted() {
		return converter;
	}

	public <T extends UIComponent> UIRender getRender(Class<T> componentType, String familly) {
		return render;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public SceneNavigator getSceneNavigator() {
		return new SceneNavigator() {
			

			@Override
			public void show(UIComponent component) {
				component.getVisibleProperty().set(true);
			}


			@Override
			public void dispose(UIComponent splash) {
				//no-op
			}

		};
	}
	        
}
