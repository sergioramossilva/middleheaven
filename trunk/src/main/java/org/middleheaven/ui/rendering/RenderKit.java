/*
 * Created on 11/08/2005
 *
 */
package org.middleheaven.ui.rendering;

import java.io.Serializable;
import java.util.Iterator;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIWindow;


/**
 * 
 * @author Sérgio M.M. Taborda
 */
public abstract class RenderKit implements Serializable{

    public interface RenderProperties extends Serializable{
        
        public String getFamilly();
        public RenderType getRenderType();
        
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
    public abstract  UIComponent renderComponent(RenderingContext context,UIComponent parent, UIComponent component);
    
    public abstract  UIComponent renderComponent(UIRender render, RenderingContext context,UIComponent parent, UIComponent component);
    
    /**
     * Encontra o <code>Render</code> apropriado para o tipo de componente pedido
     * A procura é feita da seguinte forma:<BR>
     * 1) Localiza a famillia de componentes, se a familia for nula ou string vazia, 
     * a familia padrão é usada. 
     * 2) Procura na familia de renderizadores, o renderizador do tipo escolhido
     * 
     * @param renderType
     * @return Render apropriado para o tipo de componente ou <code>null</code> se nenhum foi encontrado.
     */
    public abstract UIRender getRender(RenderType componentType, String familly);
    
    
    public abstract void addRender(UIRender render, RenderType componentType, String familly);
    

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
