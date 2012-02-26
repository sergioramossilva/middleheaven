/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 
 */
public class BindingMap {

	
	private Map<String , Map<Key, Binding>> bindings = new HashMap<String , Map<Key, Binding>>();

	
	private Map<Key, Binding> getMap(Class<?> contract){
		Map<Key, Binding> map = bindings.get(contract.getName());
		
		if (map == null){
			map = new HashMap<Key, Binding>();
			bindings.put(contract.getName(), map);
		}
		
		return map;
	}
	
	/**
	 * @param binding
	 */
	public void add(Binding binding) {
		getMap(binding.getAbstractType()).put(binding.getKey(), binding);
	}

	/**
	 * @param binding
	 */
	public void remove(Binding binding) {
		getMap(binding.getAbstractType()).remove(binding.getKey());
	}
	
	public Binding findNearest(WiringSpecification<?> query){
		Map<Key, Binding> map = getMap(query.getContract());
		
		if (map.isEmpty()){
			return null;
		}
		
		if (map.size() == 1){
			return map.values().iterator().next();
		} else {
			
			if(query.getParams().isEmpty()){
				throw new BindingException("To many matching binding candidates for " + query.getContract() + ". Try qualify your search.");
			}
			
			int max = 0;
			Binding candidate = null;
			for (Map.Entry<Key, Binding> entry : map.entrySet()){
				int points = point(query.getParams(), entry.getValue().getParams());
				
				if (points > max) {
					candidate = entry.getValue();
				}
				
			}
			
			return candidate;
		}
	}

	
	private int point(Map<String, Object> queryParams, Map<String, Object> candidateParams){
		int max = queryParams.size();
		
		for (Map.Entry<String, Object> entry : queryParams.entrySet()){
			
			if (!candidateParams.containsKey(entry.getKey())){
				max--;
			} else if (!entry.getValue().equals(candidateParams.get(entry.getKey()))){
				max--;
			}
			
		}
		
		return max;
	}

	/**
	 * 
	 */
	public Collection<Binding> getBindings() {
		Collection<Binding> all = new LinkedList<Binding>();
		
		for (Map<?, Binding> m : this.bindings.values()){
			
			all.addAll(m.values());
		}
		
		return all;
	}
}
