package org.middleheaven.model;

import java.util.LinkedList;
import java.util.List;

import org.middleheaven.domain.model.ModelSetItemBuilder;

/**
 * Composite model reader.
 * The final result is composed by the results of all registered readers.
 */
public class CompositeModelReader<B extends ModelSetItemBuilder> implements ModelReader<B> {

	private final List<ModelReader> readers = new LinkedList<ModelReader>();

	public CompositeModelReader (){}

	public CompositeModelReader<B> add(ModelReader<? extends B> reader){
		this.readers.add(reader);
		return this;
	}
	
	public CompositeModelReader<B> remove(ModelReader<? extends B> reader){
		this.readers.remove(reader);
		return this;
	}

	@Override
	public void read(Class<? extends Object> type, B builder) {
		for (ModelReader reader : readers){
			reader.read(type, builder);
		}
	}
}
