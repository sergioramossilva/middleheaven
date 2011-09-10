package org.middleheaven.ui.web.html;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.web.AbstractHtmlRender;

public class HtmlCommandSetRender extends AbstractHtmlRender {


	@Override
	public void write(JspWriter writer, RenderingContext context,
			UIComponent component) throws IOException {

		writer.append("<div class=\"commandSet\" ");
		writer.append(" id=\"" + component.getGID() + "\"");
		writer.append(">");
		
		for (UIComponent c : component.getChildrenComponents()){
			
			AbstractHtmlRender render = (AbstractHtmlRender)context.getRenderKit().getRender(c.getType(), c.getFamily());
			
			render.write(writer, context, c);
			
		}
		//writer.append(context.getAttribute(ContextScope.RENDERING, "body", String.class));
		writer.append("</div>");
	}

}
