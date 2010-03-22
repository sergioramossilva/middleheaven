package org.middleheaven.ui.web.tags;

import java.util.Arrays;
import java.util.Collection;
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

	public void setParams(Object params){
		if (params instanceof String){
			if (params.toString().contains(",")){
				this.params.addAll(Arrays.asList(params.toString().split(",")));
			} else {
				this.params.add(params);
			}
		} else if (params instanceof Collection){
			this.params.addAll((Collection)params);
		} else if (params.getClass().isArray()){
			this.params.addAll(Arrays.asList((Object[])params));
		} else {
			this.params.add(params);
		}
	}

	public int doStartTag() {
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

	// used by MessageLocationParam
	void addParam(Object obj) {
		params.add(obj);
	}
	
	@Override
	public void release() {
		super.release();
		params.clear();
	}
}