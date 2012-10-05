package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.models.UIFieldInputModel;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Text input render
 */
public class HtmlTextInputRender extends AbstractHtmlInputRender {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 * @param b
	 */
	public HtmlTextInputRender( ) {

	}


	
	@Override
	public void write(HtmlDocument document, RenderingContext context,UIComponent component) throws IOException {

		UIFieldInputModel model = (UIFieldInputModel) component.getUIModel();
		UIField comp = (UIField)component;


		UIReadState state = comp.getReadState();

		if (state == null){
			state = UIReadState.INPUT_ENABLED;
			
		}
		Writer writer = document.getBodyWriter();
		
		
		String value = "";
		if (model.getValue() != null){
			if (model.getFormater() == null){
				value = model.getValue().toString();
			} else {
				value =model.getFormater().format(model.getValue());
			}
		}
		
	
		if (!state.isVisible()){
			writer.append("<input ")
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
			.append(" class=\"mh-ui-input\"" )
			.append(" uiType=\"").append("input").append("\"");
			
			writer.append(" type=\"text\"");
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
