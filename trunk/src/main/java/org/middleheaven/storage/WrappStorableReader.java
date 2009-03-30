package org.middleheaven.storage;

import org.middleheaven.domain.EntityModel;

public final class WrappStorableReader implements StorableModelReader {

	@Override
	public StorableEntityModel read(EntityModel model) {
		return new DecoratorStorableEntityModel(model);
	}

}
