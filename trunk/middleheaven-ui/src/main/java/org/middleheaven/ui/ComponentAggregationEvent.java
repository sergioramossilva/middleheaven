package org.middleheaven.ui;

/**
 * Aggregation Event.
 */
public class ComponentAggregationEvent {

	private UIComponent component;
	private UILayoutConstraint constraint;
	
	/**
	 * 
	 * Constructor.
	 * @param component the added/removed component, the associated constraint.
	 * @param constraint
	 */
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
