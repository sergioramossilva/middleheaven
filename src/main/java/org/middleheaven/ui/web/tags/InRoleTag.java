package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.aas.AccessRequest;
import org.middleheaven.aas.Subject;
import org.middleheaven.ui.ContextScope;


public class InRoleTag extends AbstractBodyTagSupport{

	
	String subjectReference = "subject";
	String roleName;
	
	public void setRole(String role){
		this.roleName = role;
	}
	
	public void setReference(String reference){
		this.subjectReference = reference;
	}
	
	private Subject getSubject(){

		AccessRequest ar =  new TagContext(this.pageContext).getAttribute(ContextScope.REQUEST, subjectReference, AccessRequest.class);
		
		return ar.getSubject();
	}
	
	public int doStartTag() throws JspException {
		if (getSubject()==null || !getSubject().isInRole(roleName)){
			return SKIP_BODY;
		}else {
			return EVAL_BODY_BUFFERED;
		}
	}
	
	String query =null;

	public int doAfterBody() throws JspException {
		  BodyContent bc = getBodyContent();
	      // get the bc as string
	      query = bc.getString();
	      // clean up
	      bc.clearBody();
	  
	    return SKIP_BODY;
	}

	public int doEndTag() throws JspException  {
		if (query!=null){
			write(query);
		}
		return EVAL_PAGE;
	}
}
