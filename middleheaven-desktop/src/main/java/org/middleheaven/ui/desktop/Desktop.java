package org.middleheaven.ui.desktop;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.ui.AbstractUIClient;
import org.middleheaven.ui.SceneNavigator;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIException;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UIDesktop;

public class Desktop extends AbstractUIClient implements UIDesktop {

	private SceneNavigator navigator;
	
	public Desktop( SceneNavigator navigator){
		this.navigator = navigator;
	}
	
	public void addComponent(UIComponent component){
		super.addComponent(component);
		
		if (component instanceof TrayIcon){
			if (SystemTray.isSupported()){
				SystemTray tray = SystemTray.getSystemTray();
				try {
					tray.add((TrayIcon)component);
				} catch (AWTException e) {
					throw new UIException(e);
				}
			}
		}
	}

	@Override
	public UISize getDisplayableSize() {
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();

		Rectangle screenRect=ge.getMaximumWindowBounds();
		
		return UISize.pixels(screenRect.width, screenRect.height);
	}

	@Override
	public void exit() {
		
		
		BootstrapService service = ServiceRegistry.getService(BootstrapService.class);
		
		service.stop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SceneNavigator getSceneNavigator() {
		return navigator;
	}





}
