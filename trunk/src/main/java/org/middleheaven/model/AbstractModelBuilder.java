package org.middleheaven.model;

import org.middleheaven.domain.model.ModelBuilder;

/**
 * Abstract implementation of a ModelBuilder
 * @param <M>
 * @param <MODELSET>
 * @param <READER>
 */
public abstract class AbstractModelBuilder<M , MODELSET extends ModelSet<M>, READER extends ModelReader> implements ModelBuilder<M, MODELSET> {

	private final CompositeModelReader modelReader = new CompositeModelReader();
	
	public AbstractModelBuilder(){
		
	}
	
	protected READER reader() {
		return (READER) modelReader;
	}
	
	public <R extends READER> void addReader(R reader){
		modelReader.add(reader);
	}
	
	public <R extends READER> void removeReader(R reader){
		modelReader.remove(reader);
	}
	
	
	
}
