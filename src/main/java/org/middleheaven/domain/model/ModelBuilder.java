package org.middleheaven.domain.model;

import org.middleheaven.domain.model.EntityModelBuilder;

public interface ModelBuilder {

	public <E> EntityModelBuilder<E> getEntity(Class<E> type);

}
