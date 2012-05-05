
package org.middleheaven.ui.rendering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.logging.Logger;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;

/**
 * 
 */
public abstract class AbstractRenderKit extends RenderKit {

    private final Map<RenderProperties, UIRender> renders;  
    
    public AbstractRenderKit(){
        renders = new HashMap<RenderProperties, UIRender>();  
    }
    
	protected boolean isRendered(UIComponent component) {
		return component.isRendered();
	}
	
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
        if (!UIClient.class.equals(component.getType())){
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
        renderedComponent.setGID(component.getGID());
        // copy family
        renderedComponent.setFamily(component.getFamily());
        //copy models
        renderedComponent.setUIModel(component.getUIModel());
        
        // if the component renders its children components then there is nothing else to do
        // otherwise render the children components and add them to it.
        if (!render.isChildrenRenderer(  context,  parent,  component)){
            // does it need a container
        	if (renderedComponent instanceof UIContainer && ((UIContainer)renderedComponent).getUIContainerLayout()!= null && !((UIContainer)renderedComponent).getUIContainerLayout().isRendered()){
            	// render layout
            	UILayout layout = (UILayout)this.renderComponent(context, component , ((UIContainer)component).getUIContainerLayout());
            	((UIContainer)renderedComponent).setUIContainerLayout(layout);
            }

            for (UIComponent child : component.getChildrenComponents()){
                UIComponent rchild = renderComponent(context,renderedComponent,child);
                
                renderedComponent.addComponent(rchild);
            }

        } 
        
        if (this.theme!=null){
            this.theme.applyTheme(renderedComponent);
        }
        return renderedComponent;
    }


    public <T extends UIComponent> UIRender getRender(Class<T> componentType, String familly) {
        
        RenderPropertiesKey key = new RenderPropertiesKey(familly, componentType);
        
        UIRender render = renders.get(key);
        
        if (render ==null){
        	Logger.onBookFor(this.getClass()).warn("No render has found for familly {0} and type {1}. Using default familly", familly, componentType);
            // search default render
            key = new RenderPropertiesKey("", componentType);
            
            render = renders.get(key);
            
        }
        return render;
        
    }

    public <T extends UIComponent>  void addRender(UIRender render, Class<T> componentType) {
        addRender(render,componentType,"");
    }


    public final <T extends UIComponent>  void addRender(UIRender render,  Class<T> componentType, String familly) {
        
        RenderPropertiesKey key = new RenderPropertiesKey(familly, componentType);
        
        renders.put(key,render);
  
    }

    public Iterator<RenderProperties> renders() {
        return renders.keySet().iterator();
    }
    
    
    protected final class RenderPropertiesKey implements RenderProperties{

		private static final long serialVersionUID = 1L;
		
		final String familly; 
        final Class<?> type;
        
        public RenderPropertiesKey(String familly , Class<?>  type){
        	if(type ==null){
        		throw new IllegalArgumentException("Type cannot be null");
        	}
            this.familly = familly==null?"":familly.trim();
            this.type = type;
        }
        
        public final String getFamilly() {
            return familly;
        }


        public final <T extends UIComponent> Class<T> getRenderType() {
            return (Class<T>) type;
        }
        
        public final boolean equals(Object other){
            return other instanceof RenderPropertiesKey && equals((RenderPropertiesKey)other);
        }
        
        public final boolean equals(RenderPropertiesKey other){
            return this.type.getName().equals(other.type.getName()) && this.familly.equals(other.familly);
        }
        
        public final int hashCode(){
            return type.hashCode() ^ this.familly.hashCode();
        }
    }

}
