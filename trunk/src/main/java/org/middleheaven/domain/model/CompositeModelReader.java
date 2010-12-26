package org.middleheaven.domain.model;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.domain.model.CompositeModelReader;
import org.middleheaven.domain.model.ModelBuilder;

public class CompositeModelReader implements DomainModelReader{

	private final List<DomainModelReader> readers = new LinkedList<DomainModelReader>();
	
	@Override
	public final void read(Class<?> type, ModelBuilder builder) {
		
		for (DomainModelReader reader : readers){
			reader.read(type, builder);
		}
		
	}
	
	public CompositeModelReader add(DomainModelReader reader){
		this.readers.add(reader);
		return this;
	}
	
	public CompositeModelReader remove(DomainModelReader reader){
		this.readers.remove(reader);
		return this;
	}

}
