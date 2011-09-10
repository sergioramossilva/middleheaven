/**
 * 
 */
package org.middleheaven.process.web.server;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.middleheaven.process.Attribute;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ContextScopeStrategy;
import org.middleheaven.process.ObjectAttribute;
import org.middleheaven.util.collections.IteratorAdapter;

class ParametersContextScopeStrategy implements ContextScopeStrategy {

	private Map<String, String> parameters;
	
	public ParametersContextScopeStrategy (Map<String, String> parameters){
		this.parameters= parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Attribute> iterator() {
		return new IteratorAdapter<Attribute, Map.Entry<String, String>>(parameters.entrySet().iterator()){

			@Override
			public Attribute adaptNext(Entry<String, String> next) {
				return new ObjectAttribute(next.getKey(), next.getValue());
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return parameters.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return parameters.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContextScope getScope() {
		return ContextScope.PARAMETERS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		Object value = parameters.get(name);
		
		if (value != null){
			// the result is an array and the expected type is array
			if (type.isArray() && value.getClass().isArray()){
				try{
					return type.cast(value);
				} catch (ClassCastException e ){
					throw new ClassCastException("Cannot cast " + value.getClass() + " to " + type);
				}
			} else if (value.getClass().isArray()){
				value = ((String[]) value)[0];
			}
		} else if (type.isArray()){ // value is null and is expected array
			value = new String[0]; // parameters can only be Strings, so an array can only of string
		}
		
		return type.cast(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, Object value) {
		throw new UnsupportedOperationException("Parameters scope is read only");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("Parameters scope is read only");
	}

	
	
}