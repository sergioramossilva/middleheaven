package org.middleheaven.ui;

import java.util.Collection;
import java.util.List;


/**
 * Abstract base visual component.  
 *
 * @author Sérgio M.M. Taborda
 */
public interface UIComponent extends  Displayable {

    /**
     * @return ID that uniquely identifies this component
     */
    public String getGID();
    
    public void setGID(String id);

    /**
     * 
     * @return <code>true</code> is this component is already rendered, i.e. if it is a 
     * proper component of a rendered hierarchy 
     */
    public boolean isRendered();
    
    /**
     * @param model this components UIModel 
     */
    public void setUIModel(UIModel model);
    
    public UIModel getUIModel();
    
    /**
     * 
     * @return component rendering type
     */
    public <T extends UIComponent> Class<T> getType();
    
    /**
     * @return component rendering family
     */
    public String getFamily();
    
   
    public void setFamily(String family);
    
    /**
     * @return this components parent component
     */
    public UIComponent getUIParent();
    

    public void setUIParent(UIComponent parent);
    
    /**
     * @return ummodifiable children components set as a <code>List</code>. Use <code>addComponent()</code> and <code>removeComponent()</code>
     * to edit the set.
     */
    public List<UIComponent> getChildrenComponents();
    
    /**
     * 
     * @return number of children components. Preferable to getChildrenComponents().size();
     */
    public int getChildrenCount();
    
    /**
     * Add a component
     * @param component component to add
     */
    public void addComponent(UIComponent component);
    
    /**
     * Remove a component
     * @param component component to remove
     */
    public void removeComponent(UIComponent component);
    
    /**
     * Executes a UIQuery upon this component.
     * @param query - expression to execute
     * @return set of encountered components
     */
    public Collection<UIComponent> findComponents(UIQuery query);
    
    public void setVisible(boolean visible);
    public boolean isVisible();
    
    public boolean isEnabled();
    
    public void gainFocus();
    public boolean hasFocus();
    
    
    public boolean equals(Object other);
    
    public int hashCode();

	void setEnabled(boolean enabled);

	


    

}
