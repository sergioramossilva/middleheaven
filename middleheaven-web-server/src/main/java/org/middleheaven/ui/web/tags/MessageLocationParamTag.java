package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class MessageLocationParamTag extends AbstractBodyTagSupport{

	Object value;
	
	public void setValue(Object obj){
		this.value = obj;
	}
	
	public int doStartTag() throws JspException {
		
		return EVAL_BODY_BUFFERED;
		
	}
	
	public int doAfterBody() throws JspException {
		  BodyContent bc = getBodyContent();
	      // get the bc as string
	      String content = bc.getString();
	      if (content !=null && !content.trim().isEmpty() ){
	    	  value = content;
	      }
	      // clean up
	      bc.clearBody();
	  
	    return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException{
		MessageLocalizationTag messageTag = this.findAncestorTag(MessageLocalizationTag.class);
		if (messageTag!=null){
			messageTag.addParam(value);
		}
		
		return super.doEndTag();
		
	}

	@Override
	public void releaseState() {
		value = null;
	}
}
