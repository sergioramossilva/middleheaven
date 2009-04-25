package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class HelloTag extends TagSupport {
	
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print("Hello there from tag libd");
			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}
	
	public int doEndTag() {
		return EVAL_PAGE;
	}
}