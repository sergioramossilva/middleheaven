package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.core.wiring.annotations.Name;
import org.middleheaven.core.wiring.annotations.Params;

/**
 * Maintains data about the wiring contract to be fulfill by the wiring context.  
 *
 * @param <T> the type that must be retrieved
 */
public class WiringSpecification<T> {

	private Class<T> contract;
	private Set<Annotation> annotations;
	private Map<String, String> params;
	private boolean shareable = true;
	private boolean required = true;

	public static <C> WiringSpecification<C> search(Class<C> contract) {
		final Map<String,String> params = Collections.emptyMap();
		final Set<Annotation> empty = Collections.emptySet();
		return search(contract, params, empty);
	}

	public static <C> WiringSpecification<C> search(Class<C> contract, Map<String,String> params) {
		Set<Annotation> empty = Collections.emptySet();
		return search(contract, params, empty);
	}

	public static <C> WiringSpecification<C> search(Class<C> contract, Map<String,String> params , Set<Annotation> annotations) {
		return new WiringSpecification<C>(contract, params,annotations);
	}	

	public static <C> WiringSpecification<C> search(Class<C> contract, Set<Annotation> annotations) {
		Map<String,String> params = Collections.emptyMap();
		return search(contract, params, annotations);
	}

	private WiringSpecification(Class<T> contract, Map<String,String> params , Set<Annotation> annotations) {
		this.contract = contract;
		this.annotations = new HashSet<Annotation> (annotations);
		this.params = params;

		for (Annotation a : this.annotations){
			if (Name.class.isAssignableFrom(a.annotationType())){
				try{
					this.params.put("name", ((Name)a).value());
				} catch (UnsupportedOperationException e){
					// the map is not editable. copy it
					this.params = new HashMap<String,String>(params);
					// try again
					this.params.put("name", ((Name)a).value());
				}
			}  else if (a.annotationType().isAnnotationPresent(Params.class)){
				Params name = a.annotationType().getAnnotation(Params.class);
				String[] paramPairs = name.value();
				for (String paramPair : paramPairs){
					String[] values = paramPair.split("=");
					if(values.length!=2){
						throw new IllegalStateException("Param pair expected to be in format name=value bu found" + paramPair);
					}
					params.put(values[0], values[1]);
				}
			}
		}
	}



	public Map<String, String > getParams(){
		return Collections.unmodifiableMap(this.params);
	}

	public String getParam(String key){
		return params.get(key);
	}

	public Class<T> getContract() {
		return contract;
	}

	public Set<Annotation> getSpecifications() {
		return Collections.unmodifiableSet(annotations);
	}

	public void setShareable(boolean shareable) {
		this.shareable = shareable;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Indicated if the resolved object can be shared. If not the context will create another object of the same type.
	 * If the type is a singleton, and exception wil be throwned. 
	 * @return {@code true} if the resolved object can be wired to other points, {@code false} otherwise.
	 */
	public boolean isShareable() {
		return shareable;
	}

	public boolean isRequired() {
		return required;
	}
}
