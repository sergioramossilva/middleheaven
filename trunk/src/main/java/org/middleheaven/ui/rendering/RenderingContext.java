package org.middleheaven.ui.rendering;

import java.util.Enumeration;

import org.middleheaven.ui.Context;
import org.middleheaven.ui.ContextScope;

public class RenderingContext implements Context{

	public RenderingContext() {
		// TODO Auto-generated constructor stub
	}
	
	public RenderingContext(Context context) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames(ContextScope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		// TODO Auto-generated method stub
		
	}

}
