package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.models.UICommandModel;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Implementation of a Button Command in HTML.
 */
public class HtmlCommandButtonRender extends AbstractHtmlRender {


	private static final long serialVersionUID = -1218761349031119712L;

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
		
		document.addRelativeStylesheet("css/ui/jquery-ui-1.8.20.custom.css");
		document.addRelativeStylesheet("css/ui/extention.css");
		
		HtmlScript jquery = new HtmlScript("jquery");
		jquery.setRelativeSource("js/jquery-1.7.2.min.js");
		document.addScript(jquery);
		
		HtmlScript jqueryUI = new HtmlScript("jquery-ui");
		jqueryUI.setRelativeSource("js/jquery-ui-1.8.20.custom.min.js");
		document.addScript(jqueryUI);
		
		document.addScript(new HtmlScript("mh-ui-exec").setRelativeSource("js/mh-ui-exec.js"));
		
		UICommandModel model = (UICommandModel) component.getUIModel();
		
		Writer writer = document.getBodyWriter();
		
		writer.append("<input  ")
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(" name=\"").append(model.getName()).append("\"")
		.append(" class=\"mh-ui-command\"" )
		.append(" uiType=\"").append("command").append("\"");
		
		//if(model.isSubmit()){
		writer.append(" type=\"button\" ");
		//} else {
		//	writer.append(" type=\"button\" ");
		//}
		
		if(!component.isEnabled()){
			writer.append(" disabled=\"disabled\" ");
		}

		writer.append(" value=\"" + this.localize(model.getText() , document.getCulture()) + "\" />");

	}

}
