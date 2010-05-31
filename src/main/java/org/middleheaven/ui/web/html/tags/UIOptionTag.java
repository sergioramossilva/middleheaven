package org.middleheaven.ui.web.html.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.ui.web.tags.AbstractBodyTagSupport;

public class UIOptionTag extends AbstractBodyTagSupport{

	private Object value;
	private String caption;

	public void setValue(Object value){
		this.value = value;
	}

	public final int doAfterBody() throws JspException{

		BodyContent bc = getBodyContent();
		// get the bc as string
		caption = bc.getString();
		// clean up
		bc.clearBody();


		return SKIP_BODY;

	}
	
	public int doEndTag() throws JspException{
		
		UISelectOneTag tag = this.findAncestorTag(UISelectOneTag.class);
		
		if(tag==null){
			throw new IllegalStateException("Option tag must be nested inside a selection tag");
		}
		
		tag.addElement(caption, value);
		
		return EVAL_PAGE;
	}

	@Override
	public void releaseState() {
		// no-op
	}
	
	

}
