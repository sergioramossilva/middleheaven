package org.middleheaven.ui.web.html;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.models.UICommandModel;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.web.AbstractHtmlRender;

public class HtmlCommandHiperLinkRender extends AbstractHtmlRender {

	@Override
	public void write(JspWriter writer, RenderingContext context,UIComponent component) throws IOException {
		
		UICommandModel model = (UICommandModel) component.getUIModel();
		
		writer.append("<a ");
		writer.append(" id=\"" + component.getGID() + "\"");

		writer.append(" onClick=\"javascript:submit()\"");
		writer.append(">");
		writer.append(model.getText());
		writer.append("</a>");
	}



}
