/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.models.UIWindowModel;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class HtmlWindowRender extends AbstractHtmlRender  {


	private static final long serialVersionUID = 589975837582085054L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
	
		UIWindowModel model = (UIWindowModel) component.getUIModel();
		
		Writer head = document.getHeadWriter();
		
		if (model.getTitle() != null){
			head.append(" <title>").append(this.localize(model.getTitle(), document.getCulture())).append("</title>");
		}
		
		Writer body = document.getBodyWriter();
		
		body.append("<div  ")
		.append(" class=\"mh-ui-window\"")
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(" uiType=\"").append("window").append("\"");
		
		body.append(">");
		
		// render children
		
		for (UIComponent c : component.getChildrenComponents()){
			((HtmlUIComponent) c).writeTo(document, context);
		}
		
		
		body.append("</div>");

	}


}
