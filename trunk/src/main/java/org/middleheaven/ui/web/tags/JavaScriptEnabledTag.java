package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class JavaScriptEnabledTag extends AbstractBodyTagSupport{

	
	public int doStartTag() throws JspException {
		write("<script ");
		writeAttribute("text", "text/javascript");
		write(" ></script>");
		return EVAL_BODY_BUFFERED;
		
	}
	
	String query;

	public int doAfterBody() throws JspException {
		  BodyContent bc = getBodyContent();
	      // get the bc as string
	      query = bc.getString();
	      // clean up
	      bc.clearBody();
	  
	    return SKIP_BODY;
	}

	public int doEndTag() throws JspException  {
		writeLine("<noscript>");
		writeLine(query);
		writeLine("</noscript>");
		return EVAL_PAGE;
	}
}
