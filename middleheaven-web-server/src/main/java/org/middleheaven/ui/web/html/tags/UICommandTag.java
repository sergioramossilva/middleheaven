package org.middleheaven.ui.web.html.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.web.tags.TagContext;


public class UICommandTag extends AbstractUIComponentBodyTagSupport{

	private String name;
	private boolean validate;
	private String caption;
	private String content = "";
	private boolean enabled = true;
	
	
	public void setName(String name) {
		this.name = name;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public final int doAfterBody() throws JspException{

		BodyContent bc = getBodyContent();
		// get the bc as string
		content = bc.getString();
		// clean up
		bc.clearBody();


		return SKIP_BODY;

	}
	
	protected void prepareRender(TagContext attributeContext,UIComponent templateComponent) {
		super.prepareRender(attributeContext , templateComponent);
		
		
		UICommand command = (UICommand) templateComponent;
		command.getEnableProperty().set(enabled);
		command.getTextProperty().set(TextLocalizable.valueOf(caption));
		
	}
	
	public int doEndTag() throws JspException{
		
		try {
			
			UICommandSetTag commandSet = this.findAncestorTag(UICommandSetTag.class);

			commandSet.addChildren(this.getUIComponent());
			return EVAL_PAGE;
		} finally {
			releaseState();
		}

		
	}
	
	@Override
	public void releaseState() {
		// no-op
	}

	@Override
	public Class<? extends UIComponent> getComponentType() {
		return UICommand.class;
	}


}
