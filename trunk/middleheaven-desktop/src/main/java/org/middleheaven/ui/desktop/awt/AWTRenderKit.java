package org.middleheaven.ui.desktop.awt;

import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIDesktopTrayIcon;
import org.middleheaven.ui.desktop.DesktopClientRender;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.SceneNavigator;
import org.middleheaven.ui.rendering.UIUnitConverter;

public class AWTRenderKit extends AbstractRenderKit{

	private static final long serialVersionUID = -6998801713950007439L;
	
	public AWTRenderKit(){
		
		this.addRender(new DesktopClientRender(), UIClient.class);
		this.addRender(new TrayIconRender(), UIDesktopTrayIcon.class);
	}
	


	@Override
	public UIUnitConverter getUnitConverted() {
		// TODO Auto-generated method stub
		return null;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public SceneNavigator getSceneNavigator() {
		return new SceneNavigator(){

			@Override
			public void show(UIComponent component) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void dispose(UIComponent splash) {
				// TODO Auto-generated method stub
				
			}
		};
	}

}
