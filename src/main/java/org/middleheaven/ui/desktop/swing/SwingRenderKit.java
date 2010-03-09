package org.middleheaven.ui.desktop.swing;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.middleheaven.core.reflection.IllegalAccessReflectionException;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIUtils;
import org.middleheaven.ui.components.UIColorInput;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIDateInput;
import org.middleheaven.ui.components.UIDesktopTrayIcon;
import org.middleheaven.ui.components.UIFieldInput;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UINumericInput;
import org.middleheaven.ui.components.UIProgress;
import org.middleheaven.ui.components.UISecretInput;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.components.UITextInput;
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
		SFieldRender fieldRender = new SFieldRender();
		this.addRender(fieldRender, UITextInput.class);
		this.addRender(fieldRender, UISecretInput.class);
		this.addRender(fieldRender, UINumericInput.class);
		this.addRender(fieldRender, UIColorInput.class);
		this.addRender(fieldRender, UIDateInput.class);
		SDropDownRender ddr = new SDropDownRender();
		
		this.addRender(ddr, UISelectOne.class);
		this.addRender(ddr, UISelectOne.class, "dropdown");
		
		this.addRender(new SListRender(), UISelectOne.class, "list");
		
		this.addRender(new SFormRender(), UIForm.class);
		this.addRender(new DesktopClientRender(), UIClient.class);
		this.addRender(new TrayIconRender(), UIDesktopTrayIcon.class);
		this.addRender(new SWindowRender(), UIWindow.class);
		this.addRender(new SSplashWindowRender(), UIWindow.class,"splash");
		this.addRender(new SLayoutRender(), UILayout.class);
		this.addRender(new SLayoutRender(), UILayout.class, "tabs");
		this.addRender(new SViewRender(), UIView.class);
		this.addRender(new SLabelRender(), UILabel.class);
		this.addRender(new SCommandRender(), UICommand.class);
		this.addRender(new SMenuRender(), UICommandSet.class,  "menu");
		this.addRender(new SToolbarRender(), UICommandSet.class,  "toolbar");
		this.addRender(new SProgressRender(), UIProgress.class);
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
		if (component==null){
			throw new IllegalArgumentException("Cannot show null component");
		}
		if (component instanceof JFrame){
			((JFrame) component).setBounds(SwingUtils.availableScreenSize());
		} else if (component instanceof JDialog){
			SwingUtils.ensureMinimumSize(((JDialog)component),null);
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
		} else if (component instanceof JWindow){
			((JWindow)component).pack();
			UIUtils.center(component);
		}
		component.setVisible(false);
	}

}
