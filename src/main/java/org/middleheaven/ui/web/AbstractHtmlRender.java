package org.middleheaven.ui.web;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public abstract class AbstractHtmlRender extends UIRender {

	@Override
	protected final UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		 return new HtmlUIComponent(component, this);

	}
	
	public abstract void write(JspWriter writer, RenderingContext context , UIComponent component) throws IOException;


	

}
