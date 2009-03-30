package org.middleheaven.domain;

import java.util.LinkedList;
import java.util.List;

public class CompositeModelReader implements ModelReader{

	private final List<ModelReader> readers = new LinkedList<ModelReader>();
	
	@Override
	public final void read(Class<?> type, ModelBuilder builder) {
		
		for (ModelReader reader : readers){
			reader.read(type, builder);
		}
		
	}
	
	public CompositeModelReader add(ModelReader reader){
		this.readers.add(reader);
		return this;
	}
	
	public CompositeModelReader remove(ModelReader reader){
		this.readers.remove(reader);
		return this;
	}

}
