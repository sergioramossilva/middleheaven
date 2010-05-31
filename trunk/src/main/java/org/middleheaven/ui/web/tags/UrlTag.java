package org.middleheaven.ui.web.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;


/**
 * Adds context information to an URL
 */
public class UrlTag extends AbstractTagSupport{

	private String href;


	public void setHref(String value) {
		this.href = value;
	}
	
	private String addContextPath(String ctx, String url){
		String result;
		if (ctx.length() > 1 && url.startsWith("/")){
			result = ctx.concat(url);
		} else {
			result = url;
		}
		
		while(result.startsWith("//")){
			result = result.substring(1);
		}
		return result;
	} 

	public int doStartTag() throws JspException {
		try {

			
			pageContext.getOut().print(addContextPath(((HttpServletRequest)pageContext.getRequest()).getContextPath(), href));

			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
}
