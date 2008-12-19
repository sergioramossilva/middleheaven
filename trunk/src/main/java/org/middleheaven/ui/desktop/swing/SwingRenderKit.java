package org.middleheaven.ui.desktop.swing;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIDesktopTrayIcon;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.desktop.DesktopClientRender;
import org.middleheaven.ui.desktop.awt.TrayIconRender;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.UIUnitConverter;

public class SwingRenderKit extends AbstractRenderKit {

	private SwingUnitConverter unitConverter = new SwingUnitConverter();
	
	public SwingRenderKit(){
			
		this.addRender(new DesktopClientRender(), UIClient.class);
		this.addRender(new TrayIconRender(), UIDesktopTrayIcon.class);
		this.addRender(new SWindowRender(), UIWindow.class);

	}
	
	@Override
	public UIUnitConverter getUnitConverted() {
		return unitConverter;
	}

	public void show(UIComponent component) {
		if (component instanceof JFrame){
			((JFrame) component).setBounds(SwingUtils.availableScreenSize());
		} else if (component instanceof JDialog){
			SwingUtils.ensureMinimumSize(((JDialog)component),null);
		}
		component.setVisible(true);
	}

	@Override
	public void dispose(UIComponent component) {
		if (component ==null){
			return;
		} else if (component instanceof JFrame){
			((JFrame) component).dispose();
		} else if (component instanceof JDialog){
			((JDialog) component).dispose();
		}
		component.setVisible(false);
	}

}