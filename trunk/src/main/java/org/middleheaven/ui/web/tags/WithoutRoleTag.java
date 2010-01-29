package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;


public class WithoutRoleTag extends AbstractRoleAssertTag{

	
	public int doStartTag() throws JspException {
		if (!isInRole(roleName)){
			return SKIP_BODY;
		}else {
			return EVAL_BODY_BUFFERED;
		}
	}
	


}
