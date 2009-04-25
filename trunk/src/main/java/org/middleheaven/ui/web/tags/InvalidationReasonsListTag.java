package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.validation.InvalidationReason;
import org.middleheaven.validation.InvalidationSeverity;
import org.middleheaven.validation.ValidationContext;


public class InvalidationReasonsListTag extends AbstractBodyTagSupport {

	Iterator<InvalidationReason> iterator;
	private String varName = "item"; 
	private ValidationContext result;
	private InvalidationSeverity severity = null;

	public void setVar(String varName){
		this.varName = varName;
	}

	public String getVar(){
		return varName;
	}

	public void setResult(ValidationContext result){
		this.result = result;
	}

	public void setReasons(Iterable<InvalidationReason> result){
		this.iterator = result.iterator();
	}

	public void setSeverity(String severity){
		this.severity = InvalidationSeverity.valueOf(severity);
	}

	public int doStartTag() throws JspException{
		if (iterator==null){
			if (severity==null){
				this.iterator = result.iterator();
			} else {
				this.iterator = result.iterator(severity);
			}
		}
		if (iterator.hasNext()){
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

	public int doEndTag() throws JspTagException {
		return EVAL_PAGE;
	}

	public void release(){
		this.iterator = null;
	}
}
