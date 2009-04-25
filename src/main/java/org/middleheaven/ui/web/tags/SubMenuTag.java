package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;


public class SubMenuTag extends AbstractTagSupport {

	
	public int doStartTag() throws JspException{
		
		MenuTag top =  this.findAncestorTag(MenuTag.class);
		
		MenuItem current = top.getCurrent();
		
		if (current.getChildrenCount()>0){
			writeLine("<submenu/>");
		}
		
		return SKIP_BODY;
	}
	

	public int doEndTag() throws JspTagException{
		
		return EVAL_PAGE;
	}
	
}
