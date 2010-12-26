package org.middleheaven.storage;

import org.middleheaven.model.domain.DomainModel;
import org.middleheaven.model.domain.EntityModel;

public final class WrappStorableReader implements StorableModelReader {

	private DomainModel domainModel;

	public WrappStorableReader(DomainModel domainModel){
		this.domainModel = domainModel;
	}
	
	@Override
	public StorableEntityModel read(Class<?> entityType) {
		
		EntityModel model = domainModel.getEntityModelFor(entityType);
		if (model == null){
			throw new RuntimeException("Model does not exist for entity " + entityType.getName());
		}
		return new DecoratorStorableEntityModel(model);
	}

}
