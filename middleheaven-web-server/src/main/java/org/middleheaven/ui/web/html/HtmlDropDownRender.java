package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;


import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.models.UISelectionModel;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of a Dropdown.
 */
public class HtmlDropDownRender extends AbstractHtmlInputRender {

	private static final long serialVersionUID = 1579436461994433298L;

	@Override
	public void write(HtmlDocument document, RenderingContext context,UIComponent component) throws IOException {
		
		UISelectionModel model = (UISelectionModel) component.getUIModel();
		
		Writer writer = document.getBodyWriter();
		
		
		writer.append("<select ")
		.append(" id=\"" + component.getGID() + "\"")
		.append(" name=\"" + model.getName() + "\"")
		.append(" class=\"mh-ui-select-one\"" )
		.append(" uiType=\"").append("select-one").append("\"")
		.append(">");
		
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
