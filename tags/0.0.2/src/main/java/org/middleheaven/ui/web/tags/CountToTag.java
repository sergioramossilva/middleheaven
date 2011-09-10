package org.middleheaven.ui.web.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class CountToTag extends AbstractBodyTagSupport {


	private static final long serialVersionUID = 5747201688615618783L;

	private int first = 0;
	private int maxCount = 0;
	private int count= 0;
	private String varName;
	
	public void setVar(String varName){
		this.varName = varName;
	}
	
	public void setTo(int maxCount){
		this.maxCount = maxCount;
	}
	
	public void setFirst(int first){
		this.first = first;
	}
	
	public final int doStartTag() throws JspException{

		
		if (hasNextElement()){
			
			count = first - 1;
			pageContext.setAttribute(varName, Integer.valueOf(count));
			return EVAL_BODY_BUFFERED;
			
		}
		releaseState();
		return SKIP_BODY;
	}
	
	
	public final void doInitBody (){
		increment();
	}
	
	public final int doAfterBody() throws JspException{
		
		try {
			getBodyContent().writeOut(getPreviousOut());
			getBodyContent().clear();
			
			if (hasNextElement()){
				increment();
				return EVAL_BODY_BUFFERED;
			}
			
			return SKIP_BODY;
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}

}
	
	private boolean hasNextElement() {
		return count < maxCount;
	}

	private void increment() {
		count++;
		pageContext.setAttribute(varName, Integer.valueOf(count));
	}

	public int doEndTag () throws JspException {
		releaseState();
		return EVAL_PAGE;
	}

	@Override
	public void releaseState() {
		count=0;
	}
	
}
