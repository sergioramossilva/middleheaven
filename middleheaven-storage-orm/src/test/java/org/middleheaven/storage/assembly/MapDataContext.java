package org.middleheaven.storage.assembly;

import java.util.Map;
import java.util.TreeMap;

import org.middleheaven.storage.types.DataContext;

public class MapDataContext  implements DataContext {

	Map<String,Object> data = new TreeMap<String,Object>();

	@Override
	public Object get(String name) {
		return data.get(name);
	}

	@Override
	public void put(String name, Object value) {
		data.put(name, value );
	} 




}
