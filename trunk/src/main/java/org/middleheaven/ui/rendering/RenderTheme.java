
package org.middleheaven.ui.rendering;

import java.io.Serializable;

import org.apache.tools.ant.types.Parameterizable;
import org.middleheaven.ui.UIComponent;


public interface RenderTheme extends Serializable, Parameterizable{
    
    
    public void installTheme (RenderKit renderKit);
    
    /**
     * Apply the theme to the component. All component are graphical except
     * for the root component.
     * @param component
     */
    public void applyTheme(UIComponent component);
    
    
    public UIRenderingStyle getRenderingStyle(String renderingStyleID);
}
