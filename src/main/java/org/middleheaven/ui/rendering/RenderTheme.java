/*
 * Created on 04/11/2005
 */
package org.middleheaven.ui.rendering;

import java.io.Serializable;

import org.apache.tools.ant.types.Parameterizable;
import org.middleheaven.ui.UIComponent;



/**
 * @author <a href="mailto:staborda@gnk.com">Sérgio M.M. Taborda</a>
 */
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
