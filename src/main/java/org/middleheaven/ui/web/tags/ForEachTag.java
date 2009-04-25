package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;


public class ForEachTag extends AbstractBodyTagSupport {

	private Iterator<?> iterator;
	private String varName = "item"; 

	public void setVar(String varName){
		this.varName = varName;
	}
	
	public void setItems(Iterable<?> iterable){
		this.iterator = iterable==null? null : iterable.iterator();
	}
	
	public int doStartTag() throws JspException{
		
		if (iterator!=null && iterator.hasNext()){
			return EVAL_BODY_BUFFERED;
		}
		return SKIP_BODY;
	}
	
	public void doInitBody (){
		Object obj = iterator.next();
		pageContext.setAttribute(varName, obj);
	}
	
	public int doAfterBody() throws JspException{
		
			try {
				getBodyContent().writeOut(getPreviousOut());
				getBodyContent().clear();
				
				if (iterator.hasNext()){
					Object obj = iterator.next();
					pageContext.setAttribute(varName, obj);
					return EVAL_BODY_BUFFERED;
				}
				
				return SKIP_BODY;
			} catch (IOException e) {
				throw new JspTagException(e.getMessage());
			}

	}
	
	public void release(){
		this.iterator = null;
	}
}
