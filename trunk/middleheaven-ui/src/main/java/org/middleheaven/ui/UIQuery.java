package org.middleheaven.ui;

import java.util.List;

public interface UIQuery {

	public UIComponent first();
	
	public <T extends UIComponent> T first(Class<T> type);
	
	public UIComponent find(int index);
	
	public <T extends UIComponent> T find(int index, Class<T> type);
	
	public List<UIComponent> list();
	
}
