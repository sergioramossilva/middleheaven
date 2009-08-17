/*
 * Created on 11/08/2005
 *
 */
package org.middleheaven.ui.rendering;

import java.io.Serializable;
import java.util.Iterator;

import org.middleheaven.ui.UIComponent;


/**
 * A set of render used to render the final UI
 */
public abstract class RenderKit implements Serializable{

    public interface RenderProperties extends Serializable{
        
        public String getFamilly();
        public <T extends UIComponent> Class<T> getRenderType();
        
    }
    
    protected RenderTheme theme;

    public RenderKit(){
      
    }
    
    public abstract Iterator<RenderProperties> renders();
    
    public RenderTheme getRenderTheme(){
        return theme;
    }
    
    public void setRenderTheme(RenderTheme theme){
        this.theme = theme;
        this.isThemeInstalled = false;
    } 
    
    private transient boolean isThemeInstalled= false;
    
    protected void installTheme(){
        if (theme!=null && !isThemeInstalled){
            this.theme.installTheme(this);
            this.isThemeInstalled = true;
        }
    }
    
    /**
     * Renderize the component determine the render automatically
     */
    public abstract  <T extends UIComponent> T renderComponent(RenderingContext context,UIComponent parent, T component);
    
    public abstract  <T extends UIComponent> T renderComponent(UIRender render, RenderingContext context,UIComponent parent, T component);
    
    /**
     * Find the appropriate  <code>Render</code> for the given component type
     * The search is made in this order:<BR>
     * 1) Find the component's familly. If its null or and empty string 
     * the default familly is used 
     * 2) Look in the renders familly the choosen render
     * 
     * @param renderType
     * @return the appropriate  <code>Render</code> for the given component type or {@code null} is none is found.
     */
    public  abstract <T extends UIComponent>  UIRender getRender(Class<T> componentType, String familly);
    
    
    public abstract <T extends UIComponent>  void addRender(UIRender render, Class<T> componentType, String familly);
    

    /**
     * 
     * @return <code>UnitConverter</code> for the rendering tencology
     */
    public abstract UIUnitConverter getUnitConverted();

	public abstract void show(UIComponent component);

	/**
	 * Dispose of the component if possible, i.e. remote it from memory
	 * @param splash
	 */
	public abstract void dispose(UIComponent splash);


   
}
