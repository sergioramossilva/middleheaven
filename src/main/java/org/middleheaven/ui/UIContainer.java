package org.middleheaven.ui;


/**
 * 
 * @author Sergio M.M. Taborda
 *
 */
public interface UIContainer extends UIComponent , UIArea {

    public void setLayout(UILayout component);
    public UILayout getLayout();
    
    public void addChildComponent (UIComponent component, Object layoutConstrain);
    
}
