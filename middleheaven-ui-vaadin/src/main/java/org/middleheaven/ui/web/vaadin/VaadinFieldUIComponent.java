/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.data.UIDataContainer;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

import com.vaadin.ui.Component;

/**
 * 
 */
public class VaadinFieldUIComponent extends VaadinOutputUIComponent implements UIField {

	private Property<Boolean> required = ValueProperty.writable("required", false);
	private Property<Integer> maxLength= ValueProperty.writable("maxLength", Integer.class);
	private Property<Integer> minLength= ValueProperty.writable("minLength", Integer.class);
	private Property<Object> value = ValueProperty.writable("value", Object.class);
	private Property<ParsableFormatter> formatter = ValueProperty.writable("formatter", ParsableFormatter.class);

	private Property<String> name = ValueProperty.writable("name", String.class);
	
	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinFieldUIComponent(Component component,
			Class<? extends UIComponent> type) {
		super(component, type);
	}

	public Property<Boolean> getRequiredProperty(){
		return required;
	}
	
	public Property<Integer> getMaxLengthProperty(){
		return maxLength;
	}
	public Property<Integer> getMinLengthProperty(){
		return minLength;
	}
	public Property<Object> getValueProperty(){
		return value;
	}
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<String> getNameProperty() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIDataContainer(UIDataContainer container) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<ParsableFormatter> getFormaterProperty() {
		return formatter;
	}

}
