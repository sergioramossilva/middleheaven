package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIColorInput;
import org.middleheaven.ui.components.UIDateInput;
import org.middleheaven.ui.components.UINumericInput;
import org.middleheaven.ui.components.UISecretInput;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class SFieldRender  extends UIRender{

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {

		if (UINumericInput.class.isAssignableFrom(component.getType()) || 
				"numeric".equals(component.getFamily())){
			return new SNumericFieldInput();
		} else if (UISecretInput.class.isAssignableFrom(component.getType()) || 
				"secret".equals(component.getFamily())){
			return new SSecretInput();
		} else if (UIDateInput.class.isAssignableFrom(component.getType()) || 
				"date".equals(component.getFamily())){
			return new SDateFieldInput();
		} else if (UIColorInput.class.isAssignableFrom(component.getType()) || 
				"color".equals(component.getFamily())){
			return new SColorFieldInput();
		} else {
			return new STextFieldInput();
		}
	}

}
