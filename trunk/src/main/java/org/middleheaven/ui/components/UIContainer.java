package org.middleheaven.ui.components;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;


/**
 * 
 * @author Sergio M.M. Taborda
 *
 */
public interface UIContainer extends UIComponent  {

    public void setLayout(UILayout component);
    public UILayout getLayout();
    
    public void addComponent (UIComponent component, UILayoutConstraint layoutConstrain);
    public void removeComponent (UIComponent component);
    
}
