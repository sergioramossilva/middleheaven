package org.middleheaven.ui.desktop.awt;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.ui.AbstractUIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIException;
import org.middleheaven.ui.components.UIDesktop;

public class Desktop extends AbstractUIClient implements UIDesktop {


	public Desktop(){}
	
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
	public UIDimension getDimension() {
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();

		Rectangle screenRect=ge.getMaximumWindowBounds();
		
		return new UIDimension(screenRect.width, screenRect.height);
	}

	@Override
	public void exit() {
		
		
		BootstrapService service = ServiceRegistry.getService(BootstrapService.class);
		
		service.stop();
	}





}
