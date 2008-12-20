package org.middleheaven.ui.components;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;


/**
 * 
 * @author Sergio M.M. Taborda
 *
 */
public interface UIContainer extends UIComponent  {

    public void setUIContainerLayout(UILayout component);
    public UILayout getUIContainerLayout();
    
    public void addComponent (UIComponent component, UILayoutConstraint layoutConstrain);
    public void removeComponent (UIComponent component);
    
}
