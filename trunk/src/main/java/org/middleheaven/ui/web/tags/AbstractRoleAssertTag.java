package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.aas.AccessRequest;
import org.middleheaven.aas.Subject;
import org.middleheaven.ui.ContextScope;


public abstract class AbstractRoleAssertTag extends AbstractBodyTagSupport{

	
	String roleName;
	
	public void setRole(String role){
		this.roleName = role;
	}
	
	private Subject getSubject(){

		AccessRequest ar =  new TagContext(this.pageContext).getAttribute(ContextScope.REQUEST, AccessRequest.class.getName(), AccessRequest.class);
		
		return ar ==null ? null : ar.getSubject();
	}
	
	protected boolean isInRole(String roleName){
		final Subject subject = getSubject();
		return subject==null || !subject.isInRole(roleName);
	}
	
	public abstract int doStartTag() throws JspException;
	
	String query =null;

	public final int doAfterBody() throws JspException {
		  BodyContent bc = getBodyContent();
	      // get the bc as string
	      query = bc.getString();
	      // clean up
	      bc.clearBody();
	  
	    return SKIP_BODY;
	}

	public final int doEndTag() throws JspException  {
		if (query!=null){
			write(query);
		}
		return EVAL_PAGE;
	}
}
