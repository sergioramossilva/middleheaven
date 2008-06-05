
package org.middleheaven.ui.rendering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.logging.Logging;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIContainer;
import org.middleheaven.ui.UILayout;

/**
 * 
 * @author Sérgio M.M. Taborda
 */
public abstract class AbstractRenderKit extends RenderKit {

    private final Map<RenderProperties, UIRender> renders;  
    
    public AbstractRenderKit(){
        renders = new HashMap<RenderProperties, UIRender>();  
    }
    
    protected abstract boolean isRendered(UIComponent component);

    public UIComponent renderComponent(RenderingContext context,UIComponent parent, UIComponent component) {
        if (component==null){
            throw new IllegalArgumentException("Cannot render null");
        }
        return renderComponent(this.getRender(component.getType(), component.getFamily()),context,parent, component);
    }
    
    public  UIComponent renderComponent(UIRender render,RenderingContext context,UIComponent parent, UIComponent component) {
        if (component==null){
            throw new IllegalArgumentException("Cannot render null component");
        }
        if (render ==null){
            throw new IllegalStateException("Render not found for component " + component.getType() + ":" + component.getFamily());
        }
        if (!component.getType().equals(RenderType.ROOT)){
            if (parent==null || !isRendered(parent)){
                throw new IllegalArgumentException("Parent component is not rendered. Parent is " + String.valueOf(parent));
            }
        }
        
        this.installTheme();
        return build(render,context,parent,component);
    }
      
    private  UIComponent build(UIRender render , RenderingContext context, UIComponent parent, UIComponent component){
       
        // render component
        UIComponent renderedComponent = render.render(context, parent, component);
        // copy IDs
        renderedComponent.setID(component.getID());
        //copy models
        renderedComponent.setUIModel(component.getUIModel());
        
        // if the component renders its children components then there is nothing else to do
        // otherwise render the children components and add them to it.
        if (!(component instanceof UILayout) && !render.isChildrenRenderer()){
            // does it need a container
            if (component instanceof UIContainer){
            	// render layout
            	UILayout layout = (UILayout)this.renderComponent(context, component , ((UIContainer)component).getLayout());
            	((UIContainer)renderedComponent).setLayout(layout);
            }

            for (UIComponent child : component.getChildrenComponents()){
                renderComponent(context,renderedComponent,child);
            }

        } 
        
        if (this.theme!=null){
            this.theme.applyTheme(renderedComponent);
        }
        return renderedComponent;
    }

    /**
     *  
     * @see br.com.gnk.fw.ui.rendering.RenderKit#getRender(RenderType, java.lang.String)
     */
    public UIRender getRender(RenderType componentType, String familly) {
        
        RenderPropertiesKey key = new RenderPropertiesKey(familly, componentType);
        
        UIRender render = renders.get(key);
        
        if (render ==null){
        	Logging.logWarn("No render has found for familly " + familly + " and type " + componentType + ". Using default familly");
            // search default render
            key = new RenderPropertiesKey("", componentType);
            
            render = renders.get(key);
            
        }
        return render;
        
    }

    public void addRender(UIRender render, RenderType componentType) {
        addRender(render,componentType,"");
    }

    /** 
     * @see br.com.gnk.fw.ui.rendering.RenderKit#addRender(br.com.gnk.fw.web.ui.Render, java.lang.String)
     */
    public final void addRender(UIRender render, RenderType componentType, String familly) {
        
        RenderPropertiesKey key = new RenderPropertiesKey(familly, componentType);
        
        renders.put(key,render);
  
    }

    public Iterator<RenderProperties> renders() {
        return renders.keySet().iterator();
    }
    
    
    protected final class RenderPropertiesKey implements RenderProperties{

		private static final long serialVersionUID = 1L;
		
		final String familly; 
        final RenderType type;
        
        public RenderPropertiesKey(String familly , RenderType type){
            this.familly = familly==null?"":familly.trim();
            this.type = type;
        }
        
        public final String getFamilly() {
            return familly;
        }


        public final RenderType getRenderType() {
            return type;
        }
        
        public final boolean equals(Object other){
            return other instanceof RenderPropertiesKey && equals((RenderPropertiesKey)other);
        }
        
        public final boolean equals(RenderPropertiesKey other){
            return this.type.equals(other.type) && this.familly.equals(other.familly);
        }
        
        public final int hashCode(){
            return type.hashCode() ^ this.familly.hashCode();
        }
    }

}
