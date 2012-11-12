/**
 * 
 */
package org.middleheaven.ui;

import java.util.List;

class ListUIQuery implements UIQuery{
	private UIComponent currentComponent;
	private String expression;

	public ListUIQuery(UIComponent currentComponent, String expression) {
		super();
		this.currentComponent = currentComponent;
		this.expression = expression;
	}

	@Override
	public UIComponent find(int index) {
		final List<UIComponent> list = list();
		return list.isEmpty() ? null : list.get(index);
	}

	@Override
	public <T extends UIComponent> T find(int index, Class<T> type) {
		return type.cast(list().get(index));
	}

	@Override
	public UIComponent first() {
		return find(0);
	}

	@Override
	public <T extends UIComponent> T first(Class<T> type) {
		return type.cast(first());
	}

	@Override
	public List<UIComponent> list() {
		return UITreeCriteria.findByExpression(currentComponent, expression);
	}

}