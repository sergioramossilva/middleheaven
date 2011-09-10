package org.middleheaven.ui.web.html;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.models.UICommandModel;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.web.AbstractHtmlRender;

public class HtmlCommandButtonRender extends AbstractHtmlRender {

	@Override
	public void write(JspWriter writer, RenderingContext context,
			UIComponent component) throws IOException {
		
		UICommandModel model = (UICommandModel) component.getUIModel();
		
		writer.append("<input  ");
		writer.append(" id=\"" + component.getGID() + "\"");

		//if(model.isSubmit()){
			writer.append(" type=\"submit\" ");
		//} else {
		//	writer.append(" type=\"button\" ");
		//}
		
		if(!model.isEnabled()){
			writer.append(" disabled=\"disabled\" ");
		}

		writer.append(" value=\"" + model.getText() + "\" />");

	}

}
