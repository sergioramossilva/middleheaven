/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.impl.UIBorderLayoutModel;
import org.middleheaven.ui.models.impl.UIBorderLayoutModel.UIBorderLayoutConstraint;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of a Layout.
 */
public class HtmlBorderLayoutRender extends AbstractHtmlRender {


	private static final long serialVersionUID = 4486173553150482265L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {

		Writer body = document.getBodyWriter();

		final UILayout uiLayout = (UILayout)component;
		UIBorderLayoutModel model = (UIBorderLayoutModel) uiLayout.getUIModel();		


		Map<String, UIComponent> mapping = new HashMap<String, UIComponent>();

		for (UIComponent c : uiLayout.getChildrenComponents()){
			mapping.put(c.getGID(), c);
		}

		UIComponent north = mapping.get(model.getBorderComponentId(UIBorderLayoutConstraint.NORTH));
		UIComponent west = mapping.get(model.getBorderComponentId(UIBorderLayoutConstraint.WEST));
		UIComponent east = mapping.get(model.getBorderComponentId(UIBorderLayoutConstraint.EAST));
		UIComponent center = mapping.get(model.getBorderComponentId(UIBorderLayoutConstraint.CENTER));
		UIComponent south = mapping.get(model.getBorderComponentId(UIBorderLayoutConstraint.SOUTH));


		// horizontal or default
		body.append("<div  ");
		body.append(" class=\"mh-ui-layout-border\"");
		body.append(" id=\"").append(component.getGID()).append("\"");
		body.append(" uiType=\"").append("layout").append("\"");

		body.append(">");
		// render children

		// north row

		body.append("<div class=\"mh-ui-layout-border-north\">");
		if (north != null){
			((HtmlUIComponent) north).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");

		// center row

		body.append("<div class=\"mh-ui-layout-border-center-pane\" >");

		// west
		body.append("<div  class=\"mh-ui-layout-border-west\">");
		if (west != null) {
			((HtmlUIComponent) west).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");

		// center
		body.append("<div  class=\"mh-ui-layout-border-center\">");

		if (center != null) {
			((HtmlUIComponent) center).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");


		// east

		body.append("<div  class=\"mh-ui-layout-border-east\">");
		if (east != null) {
			((HtmlUIComponent) east).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");


		body.append("</div>");



		// south  row

		body.append("<div class=\"mh-ui-layout-border-south\">");
		if (south != null){
			((HtmlUIComponent) south).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");

		body.append("</div>");


	}


}
