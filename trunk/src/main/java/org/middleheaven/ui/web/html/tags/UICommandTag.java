package org.middleheaven.ui.web.html.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.global.text.GlobalLabel;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIModel;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.models.UICommandModel;
import org.middleheaven.ui.web.HtmlComandButtonModel;
import org.middleheaven.ui.web.tags.TagContext;


public class UICommandTag extends AbstractUIComponentBodyTagSupport{

	private String name;
	private boolean validate;
	private String caption;
	private String content = "";
	private UICommandModel model = new HtmlComandButtonModel();
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
	
	protected void prepareRender(TagContext attributeContext) {
		super.prepareRender(attributeContext);
		
		model.setEnabled(enabled);
		model.setText(caption);
		
	}
	
	public int doEndTag() throws JspException{
		
		try {
			
			this.model.setEnabled(this.enabled);
			
			if (this.caption != null && !caption.trim().isEmpty() ){
				this.model.setText(this.localize(GlobalLabel.of(caption)));
			} else {
				this.model.setText(this.content);
			}
	
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

	@Override
	public UIModel getModel() {
		return model;
	}

	public void setModel(UICommandModel model ){
		this.model = model;
	}
}
