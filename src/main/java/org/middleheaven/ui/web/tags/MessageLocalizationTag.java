package org.middleheaven.ui.web.tags;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.middleheaven.global.text.GlobalLabel;
import org.middleheaven.ui.ContextScope;

public class MessageLocalizationTag extends AbstractTagSupport {

	private List<Object> params = new LinkedList<Object>();
	private String key;
	private String scope;

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public int doStartTag() {
		params.clear();
		return EVAL_BODY_INCLUDE;

	}

	public int doEndTag() throws JspException {
		Object[] paramsObj = new Object[params.size()];


		paramsObj = params.toArray(paramsObj);			



		if(scope==null) {
			write(localize(new GlobalLabel( key,paramsObj), null));
		} else {
			write(localize(new GlobalLabel( key,paramsObj),ContextScope.valueOf(scope)));
		}

		return EVAL_PAGE;
	}

	protected void addParam(Object obj) {
		params.add(obj);
	}
	
	@Override
	public void release() {
		super.release();
		params.clear();
	}
}