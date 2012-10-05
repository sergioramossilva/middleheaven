package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;


import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.models.UIFieldInputModel;
import org.middleheaven.ui.rendering.RenderingContext;

public class HtmlSecretRender extends AbstractHtmlInputRender {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 * @param b
	 */
	public HtmlSecretRender( ) {

	}

	@Override
	public void write(HtmlDocument document, RenderingContext context,UIComponent component) throws IOException {

		UIFieldInputModel model = (UIFieldInputModel) component.getUIModel();
		UIField comp = (UIField)component;


		UIReadState state = comp.getReadState();

		Writer writer = document.getBodyWriter();
		
		String value = model.getValue() == null ? "" : model.getValue().toString();  
		
		if (!state.isVisible()){
			writer.append("<input ")
			.append(" type=\"hidden\"")
			.append(" id=\"" + component.getGID() + "\"")
			.append(" name=\"" + model.getName() + "\"")
			.append(" value=\"" + value + "\"")
			.append(" type=\"hidden\"")
			.append(" class=\"mh-ui-input\"" )
			.append(" uiType=\"").append("input").append("\"")
			.append("/>");
		} else if (!state.isEditable()){
			writer.append("<input ");
			writer.append(" id=\"" + component.getGID() + "\"");
			writer.append(" name=\"" + model.getName() + "\"");
			writer.append(" value=\"" + value + "\"");
			writer.append(" type=\"hidden\"");
			writer.append("/>");
			writer.append("<span class=\"readOnlyField\"");
			writer.append(" id=\"" + component.getGID() + "\"")
			.append(" class=\"mh-ui-input\"" )
			.append(" uiType=\"").append("input").append("\"")
			.append(">")
			.append(model.getValue().toString())
			.append("</span>");
		} else {
			writer.append("<input ");
			writer.append(" id=\"" + component.getGID() + "\"");
			writer.append(" name=\"" + model.getName() + "\"");
			writer.append(" value=\"" + value + "\"")
			.append(" type=\"password\"")
			.append(" class=\"mh-ui-input\"" )
			.append(" uiType=\"").append("input").append("\"");
			
			if (!state.isEnabled()){
				writer.append(" disabled=\"disabled\" ");
			}
			
			if (model.getMaxLength()  > 0 ){
				writer.append(" maxlength=\"" + model.getMaxLength() + "\"");
			}
			
			writer.append("/>");
		}

		
	

		


		



	}

}
