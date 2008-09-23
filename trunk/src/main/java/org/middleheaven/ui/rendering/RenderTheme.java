
package org.middleheaven.ui.rendering;

import java.io.Serializable;

import org.apache.tools.ant.types.Parameterizable;
import org.middleheaven.ui.UIComponent;


public interface RenderTheme extends Serializable, Parameterizable{
    
    
    public void installTheme (RenderKit renderKit);
    
    /**
     * Aplica o tema ao componente. Todos os componentes
     * são componentes gráficos excepto para o root.
     * 
     * @param component
     */
    public void applyTheme(UIComponent component);
    
    
    public UIRenderingStyle getRenderingStyle(String renderingStyleID);
}
