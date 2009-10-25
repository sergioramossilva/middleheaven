package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;


public class ForEachTag extends AbstractBodyTagSupport {

	private Iterator<?> iterator;
	private String varName = "item"; 
	private int first = 0;
	private int maxCount = 0;
	private int count= 0;
	
	public void setMaxCount(int maxCount){
		this.maxCount = maxCount;
	}
	
	
	public void setFirst(int first){
		this.first = first;
	}
	
	public void setVar(String varName){
		this.varName = varName;
	}
	
	public void setItems(Iterable<?> iterable){
		this.iterator = iterable==null? null : iterable.iterator();
	}
	
	public int doStartTag() throws JspException{
	
		if (iterator!=null && iterator.hasNext()){
			
			// bias iteration
			for (int i=0; i < first && iterator.hasNext(); i++){
				iterator.next();
			}
			
			return iterator.hasNext() ? EVAL_BODY_BUFFERED : SKIP_BODY;
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
				
				if ((maxCount == 0 || ++count < maxCount) && iterator.hasNext()){
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
		this.count = 0;
		this.iterator = null;
	}
}
