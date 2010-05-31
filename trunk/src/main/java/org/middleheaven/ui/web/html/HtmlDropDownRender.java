package org.middleheaven.ui.web.html;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.models.UISelectionModel;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.web.AbstractHtmlRender;

public class HtmlDropDownRender extends AbstractHtmlRender {


	@Override
	public void write(JspWriter writer, RenderingContext context,UIComponent component) throws IOException {
		
		UISelectionModel model = (UISelectionModel) component.getUIModel();
		
		writer.append("<select ");
		writer.append(" id=\"" + component.getGID() + "\"");
		writer.append(" name=\"" + model.getName() + "\"");
		writer.append(">");
		
		for (int i=0; i < model.getSize();i++){
			
			Object element = model.getElementAt(i);
			writer.append("<option value=\"")
			.append(element.toString())
			.append("\" ");
			
			if(model.isSelectedIndex(i)){
				writer.append(" selected=\"selected\"");
			}
			
			writer
			.append(" >")
			.append(model.getFormater().format(element))
			.append("</option>");
			
			
		}
		
		
		writer.append("</select>");
	}

}
