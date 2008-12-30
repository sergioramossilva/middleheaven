package org.middleheaven.ui.desktop.swing;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.middleheaven.core.reflection.IllegalAccessReflectionException;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIDesktopTrayIcon;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.desktop.DesktopClientRender;
import org.middleheaven.ui.desktop.awt.TrayIconRender;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.UIUnitConverter;

public class SwingRenderKit extends AbstractRenderKit {

	private SwingUnitConverter unitConverter = new SwingUnitConverter();
	
	public SwingRenderKit(){
		
		setLookandFeel();
		
		this.addRender(new DesktopClientRender(), UIClient.class);
		this.addRender(new TrayIconRender(), UIDesktopTrayIcon.class);
		this.addRender(new SWindowRender(), UIWindow.class);
		this.addRender(new SLayoutRender(), UILayout.class);
		this.addRender(new SViewRender(), UIView.class);
		this.addRender(new SLabelRender(), UILabel.class);
		this.addRender(new SCommandRender(), UICommand.class);
		this.addRender(new SMenuRender(), UICommandSet.class,  "menu");
	}
	
	protected void setLookandFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			//no-op just don't change
		} catch (InstantiationException e) {
			//no-op just don't change
		} catch (IllegalAccessException e) {
			throw new IllegalAccessReflectionException(e);
		} catch (UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		
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
