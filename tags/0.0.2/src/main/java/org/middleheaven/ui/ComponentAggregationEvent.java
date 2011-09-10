package org.middleheaven.ui;

public class ComponentAggregationEvent {

	private UIComponent component;
	private UILayoutConstraint constraint;
	
	public ComponentAggregationEvent(UIComponent component,
			UILayoutConstraint constraint) {
		super();
		this.component = component;
		this.constraint = constraint;
	}

	public UIComponent getComponent() {
		return component;
	}

	public UILayoutConstraint getConstraint() {
		return constraint;
	}

}
