package org.middleheaven.storage.assembly;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.validation.Consistencies;

public class AssemblyContext implements Iterable<Data> {

	private Map<String , Data > map = new TreeMap<String,Data>();
	private Class<?> targetType;
	

	public static AssemblyContext contextualize(Object object){
		Consistencies.consistNotNull(object);
		AssemblyContext a = new AssemblyContext(object.getClass());
		a.put(object.getClass().getName(), object);
		return a;
	}
	
	private AssemblyContext(Class<? extends Object> targetType) {
		this.targetType = targetType;
	}

	
	public Class<?> assemblyTargetType(){
		return targetType;
	}
	
	@Override
	public Iterator<Data> iterator() {
		return new ArrayList<Data>(map.values()).iterator();
	}

	public void remove(Data data){
		map.remove(data.getName());
	}
	
	public boolean isUnAssembable(Object object){
		return 
				object.getClass().isPrimitive() || 
				String.class.isInstance(object)|| 
				Date.class.isInstance(object) ||
				Number.class.isInstance(object) ||
				Boolean.class.isInstance(object)
		;
		
	}

	public void put(Data data) {
		map.put(data.getName(), data);
	}
	
	public void put(String name, Object value) {
		map.put(name, new Data(name,value));
	}

	public Object get(String name) {
		Data d = map.get(name);
		if (d==null){
			return null;
		}
		return d.getValue();
	}

	public Object take(String name) {
		Data d = map.remove(name);
		if (d==null){
			return null;
		}
		return d.getValue();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}
}
